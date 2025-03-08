package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitRepository extends JpaRepository<Limit, Integer> {
}
