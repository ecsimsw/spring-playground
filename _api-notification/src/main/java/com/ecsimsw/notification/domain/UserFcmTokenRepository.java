package com.ecsimsw.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {

}
