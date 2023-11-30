package com.example.expenseadvisor.auth.service;

import com.example.expenseadvisor.exception.domain.CustomException;
import com.example.expenseadvisor.exception.domain.ErrorCode;
import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_MEMBER_NOT_EXISTS));
        return createUser(member);
    }

    private User createUser(Member member) {
        return new User(member.getEmail(), member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
