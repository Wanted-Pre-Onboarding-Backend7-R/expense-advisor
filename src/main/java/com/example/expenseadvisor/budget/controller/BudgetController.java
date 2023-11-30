package com.example.expenseadvisor.budget.controller;

import com.example.expenseadvisor.budget.dto.BudgetCreateRequest;
import com.example.expenseadvisor.budget.dto.BudgetPatchRequest;
import com.example.expenseadvisor.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
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

    /**
     * 예산 수정 API
     * 사용자는 자신이 수정하길 원하는 예산에 대한 수정 요청을 보낼 수 있다.
     *
     * @param budgetPatchRequest
     * @param userDetails
     * @return
     */
    @PatchMapping("/api/budgets")
    public ResponseEntity<Void> patchBudget(
            @Valid @RequestBody BudgetPatchRequest budgetPatchRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        budgetService.patchBudgets(budgetPatchRequest, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
