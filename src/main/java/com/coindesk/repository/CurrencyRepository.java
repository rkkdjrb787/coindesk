package com.coindesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coindesk.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
