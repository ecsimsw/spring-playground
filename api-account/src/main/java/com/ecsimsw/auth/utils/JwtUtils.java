package com.ecsimsw.auth.utils;

import com.ecsimsw.auth.exception.AuthException;
import com.ecsimsw.common.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    public static String generate(String secretKey, int expiredTime, Map<String, Object> claims) {
        var issuedAt = new Date();
        var accessTokenExpiredAt = new Date(System.currentTimeMillis() + expiredTime);
        return createToken(secretKey, claims, issuedAt, accessTokenExpiredAt);
    }

    private static String createToken(String secretKey, Map<String, Object> claims, Date issuedAt, Date expiredAt) {
        return Jwts.builder()
            .addClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(expiredAt)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public static String getClaimValue(String secretKey, String token, String key) {
        var claims = getClaims(secretKey, token);
        return String.valueOf(claims.get(key));
    }

    public static Claims getClaims(String secretKey, String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }
}
