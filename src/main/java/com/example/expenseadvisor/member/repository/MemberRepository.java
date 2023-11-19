package com.example.expenseadvisor.member.repository;

import com.example.expenseadvisor.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
