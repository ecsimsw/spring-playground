package com.ecsimsw.notification.service.client;

import com.ecsimsw.notification.config.NotificationConfig;
import com.ecsimsw.notification.domain.form.EmailForm;
import com.ecsimsw.notification.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Primary
@Profile("prod")
@RequiredArgsConstructor
@Service
public class SmtpMailClient implements EmailClient {

    private final NotificationConfig notificationConfig;
    private final JavaMailSender mailSender;

    public void send(String dest, EmailForm form, String... args) {
        EmailUtils.sendEmail(
            mailSender,
            notificationConfig.sender,
            dest,
            form.subject(),
            form.body(args)
        );
    }
}
