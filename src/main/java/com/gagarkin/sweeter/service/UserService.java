package com.gagarkin.sweeter.service;

import com.gagarkin.sweeter.domain.Role;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb.isPresent())
            return false;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        sendMessage(user);

        return true;
    }

    public boolean activateUser(String code) {

        Optional<User> userFromDb = userRepository.findByActivationCode(code);
        if (userFromDb.isPresent()) {
            User user = userFromDb.get();
            user.setActivationCode(null);
            user.setActive(true);

            userRepository.save(user);

            return true;
        }

        return false;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key))
                user.getRoles().add(Role.valueOf(key));
        }


        userRepository.save(user);
    }

    public void updateProfile(User user, String password, String email) {

        String userEmail = user.getEmail();
        boolean isEmailChanged = email != null && !email.equals(userEmail) || userEmail != null && userEmail.equals(email);
        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            user.setActive(false);
        }

        boolean isPasswordChanged = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordChanged)
            user.setPassword(passwordEncoder.encode(password));

        if (isEmailChanged)
            sendMessage(user);

        userRepository.save(user);

    }

    private void sendMessage(User user) {
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Sweeter. Please, visit next link:" +
                        "http://localhost:8080/activate/%s ",
                user.getUsername(),
                user.getActivationCode()
        );

        mailService.send(user.getEmail(), "Activation code", message);
    }

}
