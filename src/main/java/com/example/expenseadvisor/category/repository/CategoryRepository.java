package com.example.expenseadvisor.category.repository;

import com.example.expenseadvisor.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
