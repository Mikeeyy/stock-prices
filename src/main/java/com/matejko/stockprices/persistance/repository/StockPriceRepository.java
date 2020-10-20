package com.matejko.stockprices.persistance.repository;

import com.matejko.stockprices.persistance.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, String> {
    Optional<StockPrice> findByStockName(String stockName);
}
