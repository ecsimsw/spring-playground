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
    private String username;
    private String email;
    private boolean isAdmin = false;
    private boolean deleted = false;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime deletedTime = null;

    @Builder
    public User(String username, String email, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public void deleted() {
        this.deleted = true;
        this.deletedTime = LocalDateTime.now();
    }
}
