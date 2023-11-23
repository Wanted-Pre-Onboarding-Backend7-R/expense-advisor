package com.example.expenseadvisor.auth.controller;

import com.example.expenseadvisor.auth.dto.LoginRequest;
import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.repository.MemberRepository;
import com.example.expenseadvisor.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    private final String testEmail = "abc@gmail.com";
    private final String testPassword = "12345";

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

    @DisplayName("올바른 email, password를 입력하면 로그인할 수 있다.")
    @Test
    void login() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest(testEmail, testPassword);

        // when, then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

}
