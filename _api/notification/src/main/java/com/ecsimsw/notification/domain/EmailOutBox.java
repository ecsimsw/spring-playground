package com.ecsimsw.notification.domain;

import com.ecsimsw.common.domain.EmailType;
import com.ecsimsw.notification.domain.support.EmailOutBoxArgumentsConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EmailOutBox {

    private static final int RETRY_COUNT = 3;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String dest;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailType type;

    @Convert(converter = EmailOutBoxArgumentsConverter.class)
    private String[] arguments;

    private int tryCount = 0;
    private boolean failed = false;
    private LocalDateTime issuedAt = LocalDateTime.now();

    @Builder
    public EmailOutBox(String dest, EmailType type, String... arguments) {
        this.dest = dest;
        this.type = type;
        this.arguments = arguments;
    }

    public void increaseTryCount() {
        this.tryCount++;
        if (tryCount > RETRY_COUNT) {
            this.failed = true;
        }
    }
}

