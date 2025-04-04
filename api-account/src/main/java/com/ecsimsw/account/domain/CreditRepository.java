package com.ecsimsw.account.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Credit c WHERE c.uid = :uid")
    Optional<Credit> findByUidWithLock(@Param("uid") Long uid);
}
