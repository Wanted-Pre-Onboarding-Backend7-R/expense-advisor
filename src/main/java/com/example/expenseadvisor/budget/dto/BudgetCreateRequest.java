package com.example.expenseadvisor.budget.dto;

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

}
