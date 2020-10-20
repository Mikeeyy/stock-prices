package com.matejko.stockprices.showcase.service;

import com.matejko.stockprices.messaging.sender.StockPriceEventSender;
import com.matejko.stockprices.service.StockPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

class StockPriceUpdateServiceTest {
    private StockPriceUpdateService updateService;

    private StockPriceEventSender eventSender;
    private StockPriceService stockPriceService;
    private FindNewStockPriceStrategy strategy;

    @BeforeEach
    void setUp() {
        eventSender = mock(StockPriceEventSender.class);
        stockPriceService = mock(StockPriceService.class);
        strategy = mock(FindNewStockPriceStrategy.class);

        updateService = new StockPriceUpdateService(eventSender, stockPriceService, strategy);
    }

    @DisplayName("Should not send any update events if no names were returned")
    @Test
    void updatePricesTestNothingReturned() {
        when(stockPriceService.getAllStockNames()).thenReturn(List.of());

        updateService.updatePrices();

        verify(stockPriceService, times(1)).getAllStockNames();
        verify(strategy, never()).findNewStockPrice(anyString());
        verify(eventSender, never()).sendEvent(any());
    }

    @DisplayName("Should look for new prices and send events for each name returned")
    @Test
    void updatePricesTest() {
        final var names = listOfString();

        when(stockPriceService.getAllStockNames()).thenReturn(names);

        updateService.updatePrices();

        verify(stockPriceService, times(1)).getAllStockNames();
        verify(strategy, times(names.size())).findNewStockPrice(anyString());
        verify(eventSender, times(names.size())).sendEvent(any());

        names.forEach(name -> verify(strategy, times(1)).findNewStockPrice(name));
    }

    private List<String> listOfString() {
        return IntStream.range(0, 1000)
                .mapToObj(integer -> UUID.randomUUID().toString())
                .collect(Collectors.toUnmodifiableList());
    }
}