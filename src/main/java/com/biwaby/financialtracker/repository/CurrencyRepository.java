package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Boolean existsByCode(String code);
    Boolean existsByLetterCode(String letterCode);
    Boolean existsByName(String name);
    Optional<Currency> findByCode(String code);
    Optional<Currency> findByLetterCode(String letterCode);
}
