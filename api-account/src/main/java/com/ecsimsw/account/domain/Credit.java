package com.ecsimsw.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Credit {

    @Id
    private Long uid;
    private Long value;

    public Credit(Long uid, Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
        this.uid = uid;
        this.value = value;
    }

    public void add(Long addition) {
        this.value += addition;
    }
}
