package com.ecsimsw.notification.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOutBoxRepository extends JpaRepository<EmailOutBox, Long> {

    Page<EmailOutBox> findAllByFailedFalse(PageRequest page);


}
