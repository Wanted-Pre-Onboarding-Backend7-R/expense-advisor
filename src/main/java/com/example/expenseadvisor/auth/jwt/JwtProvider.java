package com.example.expenseadvisor.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORITIES_DELIMITER = ",";
    private final long tokenValidityInMilliSeconds;
    private final SecretKey secretKey;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.tokenValidityInMilliSeconds = tokenValidityInSeconds * 1000;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Authentication으로부터 JWT 토큰을 생성하고 리턴
     *
     * @param authentication JWT 토큰을 발생하고 싶은 대상 인증 객체
     * @return JWT
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(AUTHORITIES_DELIMITER));

        long now = new Date().getTime();
        Date expiration = new Date(now + this.tokenValidityInMilliSeconds);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(secretKey)
                .expiration(expiration)
                .compact();
    }

}
