package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.enums.CategoryType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CategoryRepository;
import com.biwaby.financialtracker.service.CategoryService;
import com.biwaby.financialtracker.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category create(Category category) {
        User user = userService.getCurrentUserEntity();
        if (categoryRepository.existsByNameAndUser(category.getName(), user)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> already exists".formatted(category.getName())
            );
        }
        category.setUser(user);
        return save(category);
    }

    @Override
    public Category getById(Long id) {
        User user = userService.getCurrentUserEntity();
        return categoryRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Category with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public Category getByName(String name) {
        User user = userService.getCurrentUserEntity();
        return categoryRepository.findByNameAndUser(name, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Category with name <%s> is not found or not allowed".formatted(name)
                )
        );
    }

    @Override
    public List<Category> getAll(Integer pageSize, Integer pageNumber) {
        User user = userService.getCurrentUserEntity();
        return categoryRepository.findAllByUser(user, PageRequest.of(pageNumber, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryUpdateDto dto) {
        Category categoryToUpdate = getById(id);
        if (categoryToUpdate.getName().equals("Common") || categoryToUpdate.getName().equals("Service")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> cannot be updated".formatted(categoryToUpdate.getName())
            );
        }

        User user = userService.getCurrentUserEntity();
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            if (!categoryRepository.existsByNameAndUser(dto.getName(), user)) {
                categoryToUpdate.setName(dto.getName());
            } else {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Category with name <%s> already exists".formatted(dto.getName())
                );
            }
        }
        if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
            categoryToUpdate.setType(CategoryType.getTypeByValue(dto.getType()));
        }
        if (dto.getDescription() != null) {
            categoryToUpdate.setDescription(dto.getDescription());
        }
        return save(categoryToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Category categoryToDelete = getById(id);
        if (categoryToDelete.getName().equals("Common") || categoryToDelete.getName().equals("Service")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> cannot be deleted".formatted(categoryToDelete.getName())
            );
        }
        categoryRepository.delete(categoryToDelete);
    }
}
