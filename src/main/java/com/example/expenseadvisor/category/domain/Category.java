package com.example.expenseadvisor.category.domain;

import lombok.Getter;

@Getter
public enum Category {

    FOOD("음식"),
    HOUSING("주거"),
    HOBBY("취미");

    private final String viewName;

    Category(String viewName) {
        this.viewName = viewName;
    }

}
