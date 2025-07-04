package com.ecsimsw.device.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {

    boolean existsByUsernameAndToken(String username, String token);
    void deleteByUsernameAndToken(String username, String token);
    List<UserFcmToken> findAllByUsername(String username);
}
