package com.example.expenseadvisor.member.service;

import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("멤버를 생성할 수 있다.")
    @Test
    void memberSave() {
        // given
        String email = "abc@gmail.com";
        String password = "1234abcde";
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when
        Long savedMemberId = memberService.createMember(memberCreateRequest);

        // then
        List<Member> all = memberRepository.findAll();
        assertThat(all).hasSize(1);
        Member foundMember = all.get(0);
        assertThat(foundMember.getEmail()).isEqualTo(email);
        // 저장된 패스워드는 인코딩되어 저장되므로 원본과 다른 값이된다.
        assertThat(foundMember.getPassword()).isNotEqualTo(password);
        assertThat(foundMember.getId()).isEqualTo(savedMemberId);
    }

    @DisplayName("이미 존재하는 이메일로 멤버 생성 시도 시 DataIntegrityViolationException을 던진다.")
    @Test
    void givenDuplicateEmail_thenThrows() {
        // given - 하나의 계정이 저장된 상태
        String email = "abc@gmail.com";
        String password = "1234abcde";
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();
        memberService.createMember(memberCreateRequest);
        List<Member> all = memberRepository.findAll();
        assertThat(all).hasSize(1);

        // when, then - 동일한 이메일의 정보로 계정 생성 시도
        assertThatThrownBy(() -> memberService.createMember(memberCreateRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
