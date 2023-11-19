package com.example.expenseadvisor.member.dto;

import com.example.expenseadvisor.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequest {

    // TODO: validation 추가
    private String email;

    // TODO: validation 추가
    private String password;

    public Member toMember(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(password);
        return Member.of(email, encodedPassword);
    }

    @Builder
    private MemberCreateRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
