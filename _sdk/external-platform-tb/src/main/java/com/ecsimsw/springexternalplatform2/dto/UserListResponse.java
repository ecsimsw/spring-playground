package com.ecsimsw.springexternalplatform2.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {
    private Result result;
    private boolean success;
    private long t;

    @Data
    public static class Result {
        private List<User> list;
    }

    @Data
    public static class User {
        private String username;
        private String uid;
    }
}

