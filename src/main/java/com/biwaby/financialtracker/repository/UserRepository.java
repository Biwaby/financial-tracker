package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
