package com.example.expenseadvisor.auth.jwt;

import com.example.expenseadvisor.auth.dto.LoginRequest;
import com.example.expenseadvisor.auth.service.AuthService;
import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.repository.MemberRepository;
import com.example.expenseadvisor.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class JwtFilterTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthService authService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    String BEARER_PREFIX = "Bearer ";
    // authenticated를 요청하는 api 엔드포인트로의 호출
    MockHttpServletRequestBuilder requestBuilder = get("/api/categories").accept(MediaType.APPLICATION_JSON);
    String testEmail = "test@gmail.com";
    String testPassword = "12345";

    @BeforeEach
    void beforeEach() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();
        memberService.createMember(memberCreateRequest);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @DisplayName("올바른 JWT를 가지고 있는 요청은 authenticated를 요구하는 엔드포인트로 접근할 수 있다.")
    @Test
    void givenJWT_thenAccessSucceed() throws Exception {
        // given - 올바른 JWT 발급
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);
        String jwt = authService.login(loginRequest);

        // when, then - 올바른 JWT를 이용하여 authenticated를 요구하는 엔드포인트로 접근할 수 있다.
        mockMvc.perform(requestBuilder.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwt))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("유효하지 않은 JWT를 가진 요청은 authenticated를 요구하는 엔드포인트로 접근할 수 없다.")
    @Test
    void givenNotValidJWT_thenAccessFail() throws Exception {
        // given - 유효하지 않은 JWT
        String jwt = "invalid_jwt";

        // when, then - 올바른 JWT를 이용하여 authenticated를 요구하는 엔드포인트로 접근할 수 있다.
        mockMvc.perform(requestBuilder.header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwt))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("JWT를 가지고 있지 않은 요청은 authenticated를 요구하는 엔드포인트로 접근할 수 없다.")
    @Test
    void givenNoJWT_thenAccessFail() throws Exception {
        // when, then - Authorizaion 헤더 없이 요청 시 Forbidden
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // TODO: JwtProvider에서 JWT의 유효성에 대한 예외 결과에 따라 세세하게 응답을 다르게 할 건지 결정하고 세세하게 응답을 한다고 결정했다면 그에 대한 테스트도 추가


}
