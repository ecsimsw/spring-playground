package com.ecsimsw.account.domain;

import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserPassword {

    @Id
    private Long userId;
    private String username;
    private String password;

    public UserPassword(PasswordEncoder encoder, Long userId, String username, String purePassword) {
        if (purePassword.length() < 5 || purePassword.length() > 20) {
            throw new UserException(ErrorType.INVALID_PASSWORD);
        }
        this.userId = userId;
        this.username = username;
        this.password = encoder.encode(purePassword);
    }

    public Long userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
