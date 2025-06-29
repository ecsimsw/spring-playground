package com.ecsimsw.notification.service;

import com.ecsimsw.notification.domain.EmailForms;
import com.ecsimsw.notification.domain.EmailType;
import com.ecsimsw.notification.domain.EmailOutBox;
import com.ecsimsw.notification.domain.EmailOutBoxRepository;
import com.ecsimsw.notification.service.client.EmailClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

import static com.ecsimsw.notification.config.ThreadPoolConfig.THREAD_POOL_NAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailClient emailClient;
    private final EmailOutBoxRepository emailOutBoxRepository;

    @Transactional
    public void outbox(String to, EmailType type, String... args) {
        var email = EmailOutBox.builder()
            .dest(to)
            .type(type)
            .arguments(args)
            .build();
        emailOutBoxRepository.save(email);
    }

    @Transactional
    public void success(EmailOutBox email) {
        emailOutBoxRepository.delete(email);
    }

    @Transactional
    public void failed(EmailOutBox email) {
        email.increaseTryCount();
        emailOutBoxRepository.save(email);
    }

    @Async(THREAD_POOL_NAME)
    public CompletableFuture<EmailOutBox> send(EmailOutBox email) {
        var form = EmailForms.formById(email.getType());
        return CompletableFuture.supplyAsync(() -> {
            emailClient.send(email.getDest(), form, email.getArguments());
            return email;
        });
    }
}
