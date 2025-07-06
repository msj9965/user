package com.example.userservice.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final int expiration_access;
    private final int expiration_refresh;
    private Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration.access-token}") int expiration_access,
                            @Value("${jwt.expiration.refresh-token}") int expiration_refresh) {
        this.secretKey = secretKey;
        this.expiration_access = expiration_access;
        this.expiration_refresh = expiration_refresh;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateAccessToken(String email, String role) {
        return createToken(email, role, Duration.ofMinutes(expiration_access));
    }

    public String generateRefreshToken(String email) {
        return createToken(email, null, Duration.ofDays(expiration_refresh));
    }
    public String createToken(String email, String role, Duration expiration) {
        //claims는 jwt 토큰의 payload에 저장되는 정보
        //subject는 토큰의 주체를 나타내며, 일반적으로 사용자 식별 정보(예: 이메일)를 사용합니다.
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now  = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration.toMillis()))
                .signWith(SECRET_KEY)
                .compact();
        return token;
    }
}
