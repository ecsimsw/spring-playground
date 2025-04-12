package com.ecsimsw.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class BlockedUser implements Serializable {

    @Id
    private String username;

    public BlockedUser(String username) {
        this.username = username;
    }
}


