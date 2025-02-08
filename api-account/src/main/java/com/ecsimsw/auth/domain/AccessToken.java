//package com.ecsimsw.auth.domain;
//
//import com.ecsimsw.account.domain.User;
//import com.ecsimsw.common.error.ErrorType;
//import com.ecsimsw.common.support.JwtUtils;
//import com.ecsimsw.common.domain.UserStatus;
//import com.ecsimsw.error.AuthException;
//
//import java.util.Map;
//
//import static com.ecsimsw.auth.config.TokenConfig.ACCESS_TOKEN_EXPIRED_TIME;
//
//public record AccessToken(
//    String username,
//    boolean isAdmin,
//    UserStatus userStatus
//) {
//    public static AccessToken of(User user) {
//        return new AccessToken(user.getUsername(), user.isAdmin(), user.getStatus());
//    }
//
//    public static AccessToken fromToken(String secretKey, String token) {
//        try {
//            return new AccessToken(
//                JwtUtils.getClaimValue(secretKey, token, "username"),
//                Boolean.parseBoolean(JwtUtils.getClaimValue(secretKey, token, "isAdmin")),
//                UserStatus.valueOf(JwtUtils.getClaimValue(secretKey, token, "status"))
//            );
//        } catch (Exception e) {
//            throw new AuthException(ErrorType.INVALID_TOKEN);
//        }
//    }
//
//    public String asJwtToken(String secretKey) {
//        return JwtUtils.generate(secretKey, ACCESS_TOKEN_EXPIRED_TIME, Map.of(
//            "username", username,
//            "isAdmin", isAdmin,
//            "status", userStatus.name()
//        ));
//    }
//}
