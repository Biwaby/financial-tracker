package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.entity.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);
    Category create(Category category);
    Category getById(Long id);
    Category getByName(String name);
    List<Category> getAll(Integer pageSize, Integer pageNumber);
    Category update(Long id, CategoryUpdateDto dto);
    void deleteById(Long id);
}
