package com.matejko.stockprices.showcase.scheduler;

import com.matejko.stockprices.showcase.service.StockPriceUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "showcase.mode", havingValue = "true")
public class StockPricesUpdateScheduler {
    private final StockPriceUpdateService stockPriceUpdateService;

    @Scheduled(cron = "${showcase.cron}")
    public void updatePrices() {
        stockPriceUpdateService.updatePrices();
    }
}
