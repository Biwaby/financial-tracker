package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
