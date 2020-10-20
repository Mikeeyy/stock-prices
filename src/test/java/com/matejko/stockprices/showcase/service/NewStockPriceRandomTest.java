package com.matejko.stockprices.showcase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class NewStockPriceRandomTest {
    private NewStockPriceRandom strategy;

    @BeforeEach
    void setUp() {
        strategy = new NewStockPriceRandom();
    }

    @DisplayName("Should return price within given boundaries for each case")
    @Test
    void findNewStockPriceTest() {
        IntStream.iterate(0, i -> i < 10000, i -> i + 1)
                .mapToObj(i -> strategy.findNewStockPrice(UUID.randomUUID().toString()))
                .forEach(this::performAssertions);
    }

    private void performAssertions(final BigDecimal price) {
        assertThat(price)
                .isNotNull()
                .isGreaterThanOrEqualTo(BigDecimal.ONE)
                .isLessThanOrEqualTo(BigDecimal.valueOf(1500));
    }
}