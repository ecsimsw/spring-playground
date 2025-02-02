package com.ecsimsw.auth.config;

public class Urls {

    public static final String[] PERMIT_URLS = new String[]{
        "/api/user/signup",
        "/api/auth/login",
        "/api/auth/reissue"
    };

    public static final String[] CONTENT_URLS = new String[]{
        "/h2-console/**"
    };

    public static final String ADMIN_URLS = "/api/admin/**";
}
