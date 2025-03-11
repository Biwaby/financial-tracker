package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
