package com.gagarkin.sweeter.service;

import com.gagarkin.sweeter.domain.Role;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.domain.dto.ProfileDto;
import com.gagarkin.sweeter.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
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

    public void updateProfile(User user, @Valid ProfileDto profileDto, BindingResult bindingResult) {

        boolean correctPassword = passwordEncoder.matches(profileDto.getPassword(), user.getPassword());
        if(!correctPassword){
            bindingResult.reject("passwordError", "Bad credentials");
            return;
        }


        boolean isEmailChanged = !profileDto.getEmail().equals(user.getEmail());
        if(isEmailChanged){
            user.setEmail(profileDto.getEmail());
        }

        boolean isPasswordChanged = !profileDto.getPassword().equals(profileDto.getPasswordNew());

        if(isPasswordChanged){
            user.setPassword(passwordEncoder.encode(profileDto.getPasswordNew()));
        }

        if(isEmailChanged || isPasswordChanged){
            user.setActive(false);
            user.setActivationCode(UUID.randomUUID().toString());
            sendMessage(user);
            userRepository.save(user);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

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

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);

        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);

        userRepository.save(user);
    }

    public void updateProfile(User user, ProfileDto profileDto) {

    }
}
