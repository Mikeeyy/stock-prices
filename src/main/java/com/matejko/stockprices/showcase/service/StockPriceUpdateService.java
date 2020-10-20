package com.matejko.stockprices.showcase.service;

import com.matejko.stockprices.messaging.sender.StockPriceEventSender;
import com.matejko.stockprices.service.StockPriceService;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public
class StockPriceUpdateService {
    private final StockPriceEventSender eventSender;
    private final StockPriceService stockPriceService;
    private final FindNewStockPriceStrategy newStockPriceStrategy;

    public void updatePrices() {
        stockPriceService.getAllStockNames()
                .stream()
                .map(this::createEvent)
                .forEach(eventSender::sendEvent);
    }

    private StockPriceDto createEvent(final String stockName) {
        return new StockPriceDto(stockName, newStockPriceStrategy.findNewStockPrice(stockName));
    }
}
