package com.example.expenseadvisor.category.dto;

import com.example.expenseadvisor.category.domain.Category;
import lombok.Getter;

@Getter
public class CategoryDto {

    private final String name;
    private final String viewName;

    public CategoryDto(Category category) {
        this.name = category.name();
        this.viewName = category.getViewName();
    }

}
