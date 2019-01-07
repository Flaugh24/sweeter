package com.gagarkin.sweeter.repository;

import com.gagarkin.sweeter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByActivationCode(String code);
}
