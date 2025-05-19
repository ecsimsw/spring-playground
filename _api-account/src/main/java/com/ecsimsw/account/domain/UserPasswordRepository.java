package com.ecsimsw.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPasswordRepository extends JpaRepository<AccountPassword, Long> {

    Optional<AccountPassword> findByUsername(String username);
}
