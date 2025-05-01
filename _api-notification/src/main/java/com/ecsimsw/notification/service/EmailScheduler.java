package com.ecsimsw.notification.service;

import com.ecsimsw.notification.domain.EmailOutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailScheduler {

    private final EmailService emailService;
    private final EmailOutBoxRepository emailOutBoxRepository;

    @Scheduled(fixedRate = 5 * 1000)
    public void sendScheduler() {
        var page = PageRequest.of(0, 10, Sort.by("issuedAt"));
        var emails = emailOutBoxRepository.findAllByFailedFalse(page);
        emails.forEach(email -> {
            var callback = emailService.send(email);
            callback.thenAccept(it -> {
                emailService.success(email);
                log.info("send email successfully : {}", email.getDest());
            }).exceptionally(e -> {
                emailService.failed(email);
                log.error("failed to send email : {}", email.getDest());
                return null;
            });
        });
    }
}
