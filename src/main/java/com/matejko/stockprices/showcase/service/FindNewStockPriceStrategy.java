package com.matejko.stockprices.showcase.service;

import java.math.BigDecimal;

interface FindNewStockPriceStrategy {
    BigDecimal findNewStockPrice(final String stockName);
}
