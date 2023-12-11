package com.example.expenseadvisor.budget.dto.response;

import com.example.expenseadvisor.budget.dto.common.BudgetByCategory;
import lombok.Getter;

import java.util.List;

@Getter
public class BudgetGetRecommendationResponse {

    private final List<BudgetByCategory> budgetByCategories;

    public BudgetGetRecommendationResponse(List<BudgetByCategory> budgetByCategories) {
        this.budgetByCategories = budgetByCategories;
    }

}
