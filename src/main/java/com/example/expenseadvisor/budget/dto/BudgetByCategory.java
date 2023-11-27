package com.example.expenseadvisor.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetByCategory {

    @NotNull
    private Long categoryId;

    @NotNull
    private Integer amount;

    public BudgetByCategory(Long categoryId, Integer amount) {
        this.categoryId = categoryId;
        this.amount = amount;
    }

}
