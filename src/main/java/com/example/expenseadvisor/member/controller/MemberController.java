package com.example.expenseadvisor.member.controller;

import com.example.expenseadvisor.member.dto.MemberCreateRequest;
import com.example.expenseadvisor.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest memberCreateRequest) {
        Long memberId = memberService.createMember(memberCreateRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();
    }

}
