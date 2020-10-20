package com.matejko.stockprices.config;

import com.matejko.stockprices.messaging.event.StockPriceEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaProducerConfig {
    private final String bootstrapServers;

    public KafkaProducerConfig(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers
        );
    }

    @Bean
    public ProducerFactory<String, StockPriceEvent> stockPriceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
                producerConfigs(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, StockPriceEvent> stockPriceEventKafkaTemplate() {
        return new KafkaTemplate<>(stockPriceProducerFactory());
    }
}
