package com.example.expenseadvisor.category.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListGetResponse {

    private final List<CategoryDto> categories;

    public CategoryListGetResponse(List<CategoryDto> categories) {
        this.categories = categories;
    }

}
