package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.enums.CategoryType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CategoryRepository;
import com.biwaby.financialtracker.repository.WalletTransactionRepository;
import com.biwaby.financialtracker.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final User TEST_USER = new User();
    private static final Category COMMON_CATEGORY = new Category(
            1L,
            TEST_USER,
            "Common",
            CategoryType.COMMON,
            "This is common category",
            new ArrayList<>()
    );
    private static final Category SERVICE_CATEGORY = new Category(
            2L,
            TEST_USER,
            "Service",
            CategoryType.SERVICE,
            "This is service category",
            new ArrayList<>()
    );
    private static final Category EXISTING_CATEGORY = new Category(
            3L,
            TEST_USER,
            "Test category",
            CategoryType.OTHER,
            "This is test category",
            List.of(new WalletTransaction())
    );

    @BeforeEach
    public void setUp() {
        TEST_USER.setCategories(new ArrayList<>());
    }

    @Test
    public void create_newCategory_createsCategory() {
        doReturn(Boolean.FALSE).when(categoryRepository).existsByNameAndUser(EXISTING_CATEGORY.getName(), EXISTING_CATEGORY.getUser());
        doReturn(EXISTING_CATEGORY).when(categoryRepository).save(EXISTING_CATEGORY);

        Category createdCategory = categoryService.create(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY);

        assertNotNull(createdCategory);
        assertThat(createdCategory).isEqualTo(EXISTING_CATEGORY);
        verify(categoryRepository, times(1)).existsByNameAndUser(any(String.class), any(User.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void create_alreadyExistsByNameAndUser_throwsException() {
        doReturn(Boolean.TRUE).when(categoryRepository).existsByNameAndUser(EXISTING_CATEGORY.getName(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.create(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CATEGORY.getName());
        verify(categoryRepository, times(1)).existsByNameAndUser(any(String.class), any(User.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getById_categoryExists_returnsFoundedCategory() {
        doReturn(Optional.of(EXISTING_CATEGORY)).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());

        Category foundCategory = categoryService.getById(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId());

        assertNotNull(foundCategory);
        assertThat(foundCategory).isEqualTo(EXISTING_CATEGORY);
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getById_categoryNotExists_throwsException() {
        doReturn(Optional.empty()).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.getById(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CATEGORY.getId().toString());
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getByName_categoryExists_returnsFoundedCategory() {
        doReturn(Optional.of(EXISTING_CATEGORY)).when(categoryRepository).findByNameAndUser(EXISTING_CATEGORY.getName(), EXISTING_CATEGORY.getUser());

        Category foundCategory = categoryService.getByName(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getName());

        assertNotNull(foundCategory);
        assertThat(foundCategory).isEqualTo(EXISTING_CATEGORY);
        verify(categoryRepository, times(1)).findByNameAndUser(any(String.class), any(User.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getByName_categoryNotExists_throwsException() {
        doReturn(Optional.empty()).when(categoryRepository).findByNameAndUser(EXISTING_CATEGORY.getName(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.getByName(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getName()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CATEGORY.getName());
        verify(categoryRepository, times(1)).findByNameAndUser(any(String.class), any(User.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void getAll_returnsAllFoundedCategories() {
        doReturn(new PageImpl<>(List.of(EXISTING_CATEGORY))).when(categoryRepository).findAllByUser(any(User.class), any(Pageable.class));

        List<Category> categories = categoryService.getAll(EXISTING_CATEGORY.getUser(), 5, 0);

        assertNotNull(categories);
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().getId()).isEqualTo(EXISTING_CATEGORY.getId());
        verify(categoryRepository, times(1)).findAllByUser(any(User.class), any(Pageable.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_categoryExists_returnsUpdatedCategory() {
        CategoryUpdateDto updateDto = new CategoryUpdateDto(
                "Edited test category",
                null,
                null
        );
        Category expectedCategory = new Category(
                EXISTING_CATEGORY.getId(),
                EXISTING_CATEGORY.getUser(),
                "Edited test category",
                EXISTING_CATEGORY.getType(),
                EXISTING_CATEGORY.getDescription(),
                EXISTING_CATEGORY.getWalletsTransactionsWithCategory()
        );
        doReturn(Optional.of(EXISTING_CATEGORY)).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());
        doReturn(Boolean.FALSE).when(categoryRepository).existsByNameAndUser(expectedCategory.getName(), EXISTING_CATEGORY.getUser());
        doReturn(expectedCategory).when(categoryRepository).save(expectedCategory);

        Category updatedCategory = categoryService.update(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId(), updateDto);

        assertNotNull(updatedCategory);
        assertThat(updatedCategory).isEqualTo(expectedCategory);
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, times(1)).existsByNameAndUser(any(String.class), any(User.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_categoryNotExists_throwsException() {
        doReturn(Optional.empty()).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.update(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CATEGORY.getId().toString());
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_categoryToUpdateNameEqualsCommonOrServiceCategoryName_throwsException() {
        doReturn(Optional.of(COMMON_CATEGORY)).when(categoryRepository).findByIdAndUser(COMMON_CATEGORY.getId(), COMMON_CATEGORY.getUser());
        doReturn(Optional.of(SERVICE_CATEGORY)).when(categoryRepository).findByIdAndUser(SERVICE_CATEGORY.getId(), SERVICE_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.update(COMMON_CATEGORY.getUser(), COMMON_CATEGORY.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(COMMON_CATEGORY.getName());
        assertThatThrownBy(() -> categoryService.update(SERVICE_CATEGORY.getUser(), SERVICE_CATEGORY.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(SERVICE_CATEGORY.getName());
        verify(categoryRepository, times(2)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void update_alreadyExistsByName_throwsException() {
        CategoryUpdateDto updateDto = new CategoryUpdateDto(
                EXISTING_CATEGORY.getName(),
                null,
                null
        );
        doReturn(Optional.of(EXISTING_CATEGORY)).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());
        doReturn(Boolean.TRUE).when(categoryRepository).existsByNameAndUser(updateDto.getName(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.update(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(updateDto.getName());
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, times(1)).existsByNameAndUser(any(String.class), any(User.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void deleteById_categoryExists_deletesCategory() {
        doReturn(Optional.of(EXISTING_CATEGORY)).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());
        doReturn(Optional.of(COMMON_CATEGORY)).when(categoryRepository).findByNameAndUser(COMMON_CATEGORY.getName(), COMMON_CATEGORY.getUser());
        doReturn(List.of()).when(walletTransactionRepository).saveAll(EXISTING_CATEGORY.getWalletsTransactionsWithCategory());
        doNothing().when(categoryRepository).delete(EXISTING_CATEGORY);

        categoryService.deleteById(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId());

        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, times(1)).findByNameAndUser(any(String.class), any(User.class));
        assertThatList(EXISTING_CATEGORY.getWalletsTransactionsWithCategory())
                .extracting(WalletTransaction::getCategory)
                .containsOnly(COMMON_CATEGORY);
        verify(walletTransactionRepository, times(1)).saveAll(any(List.class));
        verify(categoryRepository, times(1)).delete(any(Category.class));
        verifyNoMoreInteractions(walletTransactionRepository, categoryRepository);
    }

    @Test
    public void deleteById_categoryNotExists_throwsException() {
        doReturn(Optional.empty()).when(categoryRepository).findByIdAndUser(EXISTING_CATEGORY.getId(), EXISTING_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.deleteById(EXISTING_CATEGORY.getUser(), EXISTING_CATEGORY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CATEGORY.getId().toString());
        verify(categoryRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, never()).delete(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(walletTransactionRepository);
    }

    @Test
    public void deleteById_categoryToDeleteNameEqualsCommonOrServiceCategoryName_throwsException() {
        doReturn(Optional.of(COMMON_CATEGORY)).when(categoryRepository).findByIdAndUser(COMMON_CATEGORY.getId(), COMMON_CATEGORY.getUser());
        doReturn(Optional.of(SERVICE_CATEGORY)).when(categoryRepository).findByIdAndUser(SERVICE_CATEGORY.getId(), SERVICE_CATEGORY.getUser());
        doReturn(Optional.of(COMMON_CATEGORY)).when(categoryRepository).findByNameAndUser(COMMON_CATEGORY.getName(), COMMON_CATEGORY.getUser());

        assertThatThrownBy(() -> categoryService.deleteById(COMMON_CATEGORY.getUser(), COMMON_CATEGORY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(COMMON_CATEGORY.getName());
        assertThatThrownBy(() -> categoryService.deleteById(SERVICE_CATEGORY.getUser(), SERVICE_CATEGORY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(SERVICE_CATEGORY.getName());
        verify(categoryRepository, times(2)).findByIdAndUser(any(Long.class), any(User.class));
        verify(categoryRepository, times(2)).findByNameAndUser(any(String.class), any(User.class));
        verify(categoryRepository, never()).delete(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(walletTransactionRepository);
    }
}
