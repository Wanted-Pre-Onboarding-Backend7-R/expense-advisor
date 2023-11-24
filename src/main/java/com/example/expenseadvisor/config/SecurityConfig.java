package com.example.expenseadvisor.config;

import com.example.expenseadvisor.auth.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.POST, "/api/members").permitAll()       // 회원 가입 엔드포인트 개방
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()    // 로그인 엔드포인트 개방
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                // https://stackoverflow.com/questions/59302026/spring-security-why-adding-the-jwt-filter-before-usernamepasswordauthentication
                // 다음과 같은 순서로 JwtFilter가 놓여진다.
                // ExceptionTranslationFilter - JwtFilter - AuthorizationFilter(이전에는 FilterSecurityInterceptor. 지금은 Deprecated 되었다.)
                // 이렇게 함으로써 JwtFilter에서 던지는 예외가 ExceptionTranslationFilter AuthenticationException, AccessDeniedException에 걸리도록 하고
                // AuthenticationException은 (Custom)AuthenticationEntryPoint에서 핸들하도록 하고
                // AccessDeniedException은 (Custom)AccessDeniedHandler에서 핸들되도록 하면 된다.
                .addFilterAfter(jwtFilter, ExceptionTranslationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
