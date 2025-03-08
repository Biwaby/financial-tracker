package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
