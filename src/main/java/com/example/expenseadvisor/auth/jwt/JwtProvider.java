package com.example.expenseadvisor.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
     * 인증된 Authentication으로부터 JWT 토큰을 생성하고 리턴
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


    /**
     * JWT로부터 parsed된 Claim 리턴
     *
     * @param jwt JWT
     * @return 유효한 JWT라면 parsed된 Claim, 유효하지 않는 JWT라면 null 리턴
     */
    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
        } catch (UnsupportedJwtException exception) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (JwtException exception) {
            log.info("잘못된 JWT 토큰입니다.");
        } catch (IllegalArgumentException exception) {
            log.info("JWT 토큰이 존재하지 않습니다.");
        }
        return null;
    }

    /**
     * JWT로부터 Authentication 생성하고 리턴
     *
     * @param jwt JWT
     * @return 유효한 JWT라면 Authentication, 유효한 JWT가 아니라면 null 리턴
     */
    public Authentication createAuthentication(String jwt) {
        Jws<Claims> claims = getClaims(jwt);
        if (claims == null) {
            return null;
        }

        Claims payload = claims.getPayload();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(payload.get(AUTHORITIES_KEY).toString().split(AUTHORITIES_DELIMITER))
                .map(SimpleGrantedAuthority::new)
                .toList();

        User principal = new User(payload.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }

}
