package com.ecsimsw.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {

    Optional<UserPassword> findByUsername(String username);

    void deleteByUsername(String username);
}
