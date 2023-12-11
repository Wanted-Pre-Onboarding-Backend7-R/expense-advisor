package com.example.expenseadvisor.budget.controller;

import com.example.expenseadvisor.budget.dto.common.BudgetByCategory;
import com.example.expenseadvisor.budget.dto.request.BudgetCreateRequest;
import com.example.expenseadvisor.budget.dto.request.BudgetPatchRequest;
import com.example.expenseadvisor.budget.dto.response.BudgetGetRecommendationResponse;
import com.example.expenseadvisor.budget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * 예산 생성 endpoint
     */
    @PostMapping("/budgets")
    public ResponseEntity<Void> createBudget(
            @Valid @RequestBody BudgetCreateRequest budgetCreateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        budgetService.createBudgets(budgetCreateRequest, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * 예산 수정 endpoint
     */
    @PatchMapping("/budgets")
    public ResponseEntity<Void> patchBudget(
            @Valid @RequestBody BudgetPatchRequest budgetPatchRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        budgetService.patchBudgets(budgetPatchRequest, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * 예산 추천 endpoint
     */
    @GetMapping("/budgets/recommendation")
    public ResponseEntity<BudgetGetRecommendationResponse> getBudgetRecommendation() {
        List<BudgetByCategory> budgetByCategories = budgetService.getBudgetRecommendation();
        return ResponseEntity.ok(new BudgetGetRecommendationResponse(budgetByCategories));
    }

}
