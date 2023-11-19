package com.example.expenseadvisor.member.controller;

import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;

    private final String email = "abc@gmail.com";
    private final String password = "abcd12345";

    @DisplayName("사용자를 생성할 수 있다.")
    @Test
    void createMember() throws Exception {
        // given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when, then
        mockMvc.perform(post("/api/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated());

        List<Member> all = memberRepository.findAll();
        assertThat(all).hasSize(1);
        Member savedMember = all.get(0);
        assertThat(savedMember.getEmail()).isEqualTo(email);
    }

    // TODO: 이미 존재하는 이메일로 사용자 생성 시도 시

}
