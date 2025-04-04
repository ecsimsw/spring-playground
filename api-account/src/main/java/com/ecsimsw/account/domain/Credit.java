package com.ecsimsw.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Credit {

    @Id
    private Long uid;
    private Long creditValue;

    public Credit(Long uid, Long creditValue) {
        if (creditValue < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.uid = uid;
        this.creditValue = creditValue;
    }

    public void add(Long addition) {
        this.creditValue += addition;
    }
}
