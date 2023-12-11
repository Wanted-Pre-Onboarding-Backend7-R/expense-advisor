package com.example.expenseadvisor.budget.service;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.budget.dto.common.BudgetByCategory;
import com.example.expenseadvisor.budget.dto.request.BudgetCreateRequest;
import com.example.expenseadvisor.budget.dto.request.BudgetPatchRequest;
import com.example.expenseadvisor.budget.repository.BudgetRepository;
import com.example.expenseadvisor.category.domain.Category;
import com.example.expenseadvisor.exception.domain.CustomException;
import com.example.expenseadvisor.exception.domain.ErrorCode;
import com.example.expenseadvisor.member.domain.Member;
import com.example.expenseadvisor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    /**
     * 예산을 수정한다.
     * 만약 생성하지 않은 예산에 대한 수정 요청이라면, 해당 예산을 생성한다.
     *
     * @param budgetPatchRequest 예산 수정 요청
     * @param email              유저 이메일
     */
    @Transactional
    public void patchBudgets(BudgetPatchRequest budgetPatchRequest, String email) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_MEMBER_NOT_EXISTS));

        patch(budgetPatchRequest, member);
    }

    /**
     * 추천 예산을 반환한다.
     */
    public List<BudgetByCategory> getBudgetRecommendation() {
        return null;
    }

    private List<Budget> toBudgetList(BudgetCreateRequest budgetCreateRequest, Member member) {
        return budgetCreateRequest.getBudgetByCategories().stream()
                .map(budgetByCategory -> new Budget(budgetByCategory.getAmount(), member, Category.valueOf(budgetByCategory.getCategory())))
                .toList();
    }

    private void patch(BudgetPatchRequest budgetPatchRequest, Member member) {
        for (BudgetByCategory budgetByCategory : budgetPatchRequest.getBudgetByCategories()) {
            Category category;
            try {
                category = Category.valueOf(budgetByCategory.getCategory());
                Optional<Budget> optionalBudget = budgetRepository.findByCategoryAndMember(category, member);
                // 이미 존재하는 예산이라면 수정을, 존재하지 않는 예산이라면 생성한다.
                optionalBudget.ifPresentOrElse(
                        budget -> budget.changeAmount(budgetByCategory.getAmount()),
                        () -> budgetRepository.save(new Budget(budgetByCategory.getAmount(), member, category))
                );
            } catch (IllegalArgumentException exception) {
                // Category.valueOf 에서 일치하는 Category를 찾을 수 없을 때 발생
                throw new CustomException(ErrorCode.CAT_NOT_EXISTS, budgetByCategory.getCategory(), exception);
            }
        }
    }
}
