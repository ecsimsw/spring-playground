package com.ecsimsw.notification.service.client;

import com.ecsimsw.notification.domain.form.EmailForm;

public interface EmailClient {

    void send(String dest, EmailForm form, String... args);
}
