package com.example.expenseadvisor.budget.service;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.budget.dto.BudgetCreateRequest;
import com.example.expenseadvisor.budget.repository.BudgetRepository;
import com.example.expenseadvisor.category.domain.Category;
import com.example.expenseadvisor.exception.CustomException;
import com.example.expenseadvisor.exception.ErrorCode;
import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createBudgets(BudgetCreateRequest budgetCreateRequest, String email) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_MEMBER_NOT_EXISTS));

        if (!budgetRepository.findByMember(member).isEmpty()) {
            throw new CustomException(ErrorCode.BUD_ALREADY_EXISTS);
        }

        List<Budget> budgetList = toBudgetList(budgetCreateRequest, member);
        budgetRepository.saveAll(budgetList);
    }

    private List<Budget> toBudgetList(BudgetCreateRequest budgetCreateRequest, Member member) {
        return budgetCreateRequest.getBudgetByCategories().stream()
                .map(budgetByCategory -> new Budget(budgetByCategory.getAmount(), member, Category.valueOf(budgetByCategory.getCategory())))
                .toList();
    }

}
