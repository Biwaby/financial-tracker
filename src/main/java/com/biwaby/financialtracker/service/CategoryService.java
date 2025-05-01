package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;

import java.util.List;

public interface CategoryService {

    Category save(Category category);
    Category create(User user, Category category);
    void createCommonCategoriesForUser(User user);
    Category getById(User user, Long id);
    Category getByName(User user, String name);
    List<Category> getAll(User user, Integer pageSize, Integer pageNumber);
    Category update(User user, Long id, CategoryUpdateDto dto);
    void deleteById(User user, Long id);
}
