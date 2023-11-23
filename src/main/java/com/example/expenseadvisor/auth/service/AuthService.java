package com.example.expenseadvisor.auth.service;

import com.example.expenseadvisor.auth.dto.LoginRequest;
import com.example.expenseadvisor.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken notAuthenticated = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        // AuthenticationManager를 사용하기 위해서는 UserDetailsService, PasswordEncoder Bean으로 노출할 필요가 있다.
        Authentication authenticated = authenticationManagerBuilder.getObject().authenticate(notAuthenticated);

        return jwtTokenProvider.createToken(authenticated);
    }

}
