package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.enums.CategoryType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CategoryRepository;
import com.biwaby.financialtracker.repository.WalletTransactionRepository;
import com.biwaby.financialtracker.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category create(User user, Category category) {
        if (categoryRepository.existsByNameAndUser(category.getName(), user)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> already exists".formatted(category.getName())
            );
        }
        category.setUser(user);
        category.getUser().getCategories().add(category);
        return save(category);
    }

    @Override
    @Transactional
    public void createCommonCategoriesForUser(User user) {
        Category commonCategory = new Category(
                null,
                user,
                "Common",
                CategoryType.COMMON,
                "This category accumulates all types of transactions, including both income and expense. It is protected from modification or deletion.",
                new ArrayList<>()
        );
        save(commonCategory);

        Category otherCategory = new Category(
                null,
                user,
                "Service",
                CategoryType.SERVICE,
                "This category refers to operations performed by the service. It is protected from modification or deletion.",
                new ArrayList<>()
        );
        save(otherCategory);
    }

    @Override
    public Category getById(User user, Long id) {
        return categoryRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Category with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public Category getByName(User user, String name) {
        return categoryRepository.findByNameAndUser(name, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Category with name <%s> is not found or not allowed".formatted(name)
                )
        );
    }

    @Override
    public List<Category> getAll(User user, Integer pageSize, Integer pageNumber) {
        return categoryRepository.findAllByUser(user, PageRequest.of(pageNumber, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category update(User user, Long id, CategoryUpdateDto dto) {
        Category categoryToUpdate = getById(user, id);
        if (categoryToUpdate.getName().equals("Common") || categoryToUpdate.getName().equals("Service")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> cannot be updated".formatted(categoryToUpdate.getName())
            );
        }

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
    public void deleteById(User user, Long id) {
        Category categoryToDelete = getById(user, id);
        List<WalletTransaction> walletsTransactionsWithDeletedCategory = categoryToDelete.getWalletsTransactionsWithCategory();
        if (categoryToDelete.getName().equals("Common") || categoryToDelete.getName().equals("Service")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Category with name <%s> cannot be deleted".formatted(categoryToDelete.getName())
            );
        }

        setCommonCategoryForWalletsTransactions(user, walletsTransactionsWithDeletedCategory);
        categoryRepository.delete(categoryToDelete);
    }

    private void setCommonCategoryForWalletsTransactions(User user, List<WalletTransaction> walletsTransactions) {
        Category commonCategory = getByName(user, "Common");
        if (!walletsTransactions.isEmpty()) {
            walletsTransactions.forEach(transaction -> transaction.setCategory(commonCategory));
            walletTransactionRepository.saveAll(walletsTransactions);
        }
    }
}
