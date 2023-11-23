package com.example.expenseadvisor.category.dto;

import com.example.expenseadvisor.category.domain.Category;
import lombok.Getter;

@Getter
public class CategoryDto {

    private final Long id;
    private final String name;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

}
