package com.example.expenseadvisor.category.controller;

import com.example.expenseadvisor.category.dto.CategoryDto;
import com.example.expenseadvisor.category.dto.CategoryListGetResponse;
import com.example.expenseadvisor.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<CategoryListGetResponse> getCategoryList() {
        List<CategoryDto> categories = categoryService.getCategoryList();
        return ResponseEntity.ok(new CategoryListGetResponse(categories));
    }

}
