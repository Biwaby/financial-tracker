package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
}
