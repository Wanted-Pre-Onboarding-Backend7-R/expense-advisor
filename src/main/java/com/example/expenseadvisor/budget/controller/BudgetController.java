package com.example.expenseadvisor.budget.controller;

import com.example.expenseadvisor.budget.dto.BudgetCreateRequest;
import com.example.expenseadvisor.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/api/budgets")
    public ResponseEntity<Void> createBudget(
            @Valid @RequestBody BudgetCreateRequest budgetCreateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        budgetService.createBudgets(budgetCreateRequest, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
