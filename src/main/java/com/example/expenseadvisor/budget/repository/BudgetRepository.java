package com.example.expenseadvisor.budget.repository;

import com.example.expenseadvisor.budget.domain.Budget;
import com.example.expenseadvisor.category.domain.Category;
import com.example.expenseadvisor.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByMember(Member member);

    Optional<Budget> findByCategoryAndMember(Category category, Member member);

    // TODO: 이것으로 최적화?
    List<Budget> findByCategoryInAndMember(Collection<Category> categories, Member member);

}
