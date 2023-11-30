package com.example.expenseadvisor.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetPatchRequest {

    @NotNull
    private List<BudgetByCategory> budgetByCategories;

    public BudgetPatchRequest(List<BudgetByCategory> budgetByCategories) {
        this.budgetByCategories = budgetByCategories;
    }

}
