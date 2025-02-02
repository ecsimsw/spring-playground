package com.ecsimsw.notification.domain.form;

public interface EmailForm {
    String subject();
    String body(String... args);
}