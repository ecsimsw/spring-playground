package com.ecsimsw.account.domain;

import com.ecsimsw.common.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByIdAndDeletedFalse(Long userId);

    Page<User> findAllByStatusAndDeleted(UserStatus userStatus, Boolean deleted, Pageable pageable);
}
