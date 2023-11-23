package com.example.expenseadvisor.category.service;

import com.example.expenseadvisor.category.dto.CategoryDto;
import com.example.expenseadvisor.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategoryList() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::from).toList();
    }

}
