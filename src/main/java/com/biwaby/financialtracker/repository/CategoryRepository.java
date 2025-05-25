package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByUser(User user, Pageable pageable);
    Optional<Category> findByIdAndUser(Long id, User user);
    Boolean existsByNameAndUser(String name, User user);
    Optional<Category> findByNameAndUser(String name, User user);
}
