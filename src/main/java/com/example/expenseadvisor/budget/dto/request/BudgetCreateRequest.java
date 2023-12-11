package com.example.expenseadvisor.budget.dto.request;

import com.example.expenseadvisor.budget.dto.common.BudgetByCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetCreateRequest {

    @NotNull
    public List<BudgetByCategory> budgetByCategories;

    public BudgetCreateRequest(List<BudgetByCategory> budgetByCategories) {
        this.budgetByCategories = budgetByCategories;
    }

}
