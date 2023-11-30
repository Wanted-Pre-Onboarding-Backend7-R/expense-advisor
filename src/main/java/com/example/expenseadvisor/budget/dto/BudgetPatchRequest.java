package com.example.expenseadvisor.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetPatchRequest {

    @NotNull(message = "수정 예산 리스트는 비어 있으면 안됩니다.")
    private List<BudgetByCategory> budgetByCategories;

    public BudgetPatchRequest(List<BudgetByCategory> budgetByCategories) {
        this.budgetByCategories = budgetByCategories;
    }

}
