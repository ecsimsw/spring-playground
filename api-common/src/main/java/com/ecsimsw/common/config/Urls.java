package com.ecsimsw.common.config;

public class Urls {

    // TODO :: 각 모듈에서 허용할 URL을 관리할 수 있는 방법은 없을까.
    public static final String[] PERMIT_URLS = new String[]{
        "**",
        "/api/user/signup",
        "/api/auth/login",
        "/api/auth/reissue",
        "/api/account/health",
    };

    public static final String[] CONTENT_URLS = new String[]{
        "/h2-console/**"
    };

    public static final String ADMIN_URLS = "/api/admin/**";
}
