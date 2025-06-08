package com.ecsimsw.common.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedUserDataRepository extends JpaRepository<BlockedUser, String> {
}
