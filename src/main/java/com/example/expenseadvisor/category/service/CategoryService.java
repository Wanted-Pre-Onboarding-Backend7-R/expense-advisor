package com.example.expenseadvisor.category.service;

import com.example.expenseadvisor.category.domain.Category;
import com.example.expenseadvisor.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

    public List<CategoryDto> getCategoryList() {
        return Arrays.stream(Category.values()).map(CategoryDto::new).toList();
    }

}
