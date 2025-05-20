package com.ecsimsw.account.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String uid;
    private String username;
    private LocalDateTime createTime = LocalDateTime.now();

    @Builder
    public User(String username, String uid) {
        this.username = username;
        this.uid = uid;
    }
}
