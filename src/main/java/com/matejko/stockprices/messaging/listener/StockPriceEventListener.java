package com.matejko.stockprices.messaging.listener;

import com.matejko.stockprices.messaging.event.StockPriceEvent;
import com.matejko.stockprices.service.StockPriceService;
import com.matejko.stockprices.shared.mapper.StockPriceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockPriceEventListener {
    private final StockPriceService stockPriceService;
    private final StockPriceMapper stockPriceMapper;

    @KafkaListener(topics = "${topics.stock.prices}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "stockPriceKafkaListenerContainerFactory")
    public void listener(final StockPriceEvent event) {
        logEventReceived(event);

        stockPriceService.updateStockPrice(stockPriceMapper.toDto(event));

        logEventPersisted(event);
    }

    private void logEventReceived(final StockPriceEvent event) {
        log.debug(String.format("Received event: %s", event));
    }

    private void logEventPersisted(final StockPriceEvent event) {
        log.debug(String.format("Persisted event: %s", event));
    }
}
