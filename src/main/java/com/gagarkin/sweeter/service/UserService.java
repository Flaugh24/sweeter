package com.gagarkin.sweeter.service;

import com.gagarkin.sweeter.domain.Role;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MailService mailService;

    public UserService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean addUser(User user){
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if(userFromDb.isPresent())
            return false;

        String hashPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashPassword);
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Sweeter. Please, visit next link:" +
                        "http://localhost:8080/activate/%s ",
                user.getUsername(),
                user.getActivationCode()
        );

        mailService.send(user.getEmail(), "Activation code", message);


        return true;
    }

    public boolean activateUser(String code) {

        Optional<User> userFromDb = userRepository.findByActivationCode(code);
        if(userFromDb.isPresent()){
            User user = userFromDb.get();
            user.setActivationCode(null);
            user.setActive(true);

            userRepository.save(user);

            return true;
        }

        return false;
    }
}
