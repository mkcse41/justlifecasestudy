package com.justlife.casestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.justlife.casestudy.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
