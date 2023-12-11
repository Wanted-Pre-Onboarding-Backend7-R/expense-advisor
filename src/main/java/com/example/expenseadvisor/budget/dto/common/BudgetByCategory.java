package com.example.expenseadvisor.budget.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetByCategory {

    @NotNull
    private String category;

    @NotNull
    private Integer amount;

    public BudgetByCategory(String category, Integer amount) {
        this.category = category;
        this.amount = amount;
    }

}
