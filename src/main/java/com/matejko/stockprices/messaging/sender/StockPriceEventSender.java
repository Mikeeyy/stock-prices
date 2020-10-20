package com.matejko.stockprices.messaging.sender;

import com.matejko.stockprices.messaging.event.StockPriceEvent;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import com.matejko.stockprices.shared.mapper.StockPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockPriceEventSender {
    private final KafkaTemplate<String, StockPriceEvent> kafkaTemplate;
    private final StockPriceMapper stockPriceMapper;
    private final String topicName;

    public StockPriceEventSender(final KafkaTemplate<String, StockPriceEvent> kafkaTemplate,
                                 final StockPriceMapper stockPriceMapper,
                                 @Value("${topics.stock.prices}") final String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.stockPriceMapper = stockPriceMapper;
        this.topicName = topicName;
    }

    public void sendEvent(final StockPriceDto dto) {
        final var event = stockPriceMapper.toEvent(dto);

        logEventSending(event);

        kafkaTemplate.send(topicName, event)
                .addCallback(
                        success -> logEventSentSuccess(event),
                        failure -> logEventSentFailure(failure, event)
                );
    }

    private void logEventSending(final StockPriceEvent event) {
        log.debug(String.format("Sending event: %s", event));
    }

    private void logEventSentSuccess(final StockPriceEvent event) {
        log.debug(String.format("Sending event succeeded: %s", event));
    }

    private void logEventSentFailure(final Throwable failure, final StockPriceEvent event) {
        log.error(String.format("Sending event failed: %s", event), failure);
    }
}
