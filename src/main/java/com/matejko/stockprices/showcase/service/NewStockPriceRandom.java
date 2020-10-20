package com.matejko.stockprices.showcase.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Component
class NewStockPriceRandom implements FindNewStockPriceStrategy {
    private final Random random = new Random();

    @Override
    public BigDecimal findNewStockPrice(final String stockName) {
        return BigDecimal.valueOf(1 + (random.nextInt(1498) + random.nextDouble()))
                .setScale(2, RoundingMode.CEILING);

    }
}
