package com.ecsimsw.account.domain;

import com.ecsimsw.common.domain.UserStatus;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.error.UserException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Embedded
    private Password password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    private boolean isAdmin = false;
    private boolean deleted = false;
    private LocalDateTime createTime = LocalDateTime.now();

    @Builder
    public User(String username, Password password, String email, boolean isAdmin, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        if (status != null) {
            this.status = status;
        }
    }

    public void createTempPassword(PasswordEncoder passwordEncoder) {
        this.password = Password.createRandomly(passwordEncoder);
    }

    public void changePassword(PasswordEncoder encoder, String password) {
        this.password = Password.encode(encoder, password);
    }

    public String getTempPassword() {
        return password.getTempPassword();
    }

    public void disapprove() {
        this.status = UserStatus.DISAPPROVED;
    }

    public void approve() {
        this.status = UserStatus.APPROVED;
    }

    public void checkApproved() {
        if (!this.status.equals(UserStatus.APPROVED)) {
            throw new UserException(ErrorType.USER_NOT_APPROVED_YET);
        }
    }

    public void deleted() {
        this.deleted = true;
    }

    public boolean isTempPassword() {
        return this.password.isTempPassword();
    }

    public boolean isApproved() {
        return status == UserStatus.APPROVED;
    }
}
