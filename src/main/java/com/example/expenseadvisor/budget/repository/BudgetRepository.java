package com.example.expenseadvisor.budget.repository;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByMember(Member member);

}
