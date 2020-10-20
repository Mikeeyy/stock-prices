package com.matejko.stockprices;

import com.matejko.stockprices.api.BaseEndpointApiHelper;
import com.matejko.stockprices.messaging.event.StockPriceEvent;
import com.matejko.stockprices.messaging.listener.StockPriceEventListener;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(topics = "${topics.stock.prices}", partitions = 1)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EndToEndTest extends BaseEndpointApiHelper {

    private Producer<String, Object> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    private StockPriceEventListener listener;

    @Value("${topics.stock.prices}")
    private String topic;

    @BeforeAll
    void setUp() {
        final Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));

        producer = new DefaultKafkaProducerFactory<>(
                configs,
                new StringSerializer(),
                new JsonSerializer<>())
                .createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

    @Test
    @DisplayName("Prices should be updated by message consumer")
    public void test1() throws Exception {
        final var referencedDtos = getAllStockPrices().toList();

        final List<StockPriceDto> newDtos = new ArrayList<>();

        for (int i = 0; i < referencedDtos.size(); i++) {
            newDtos.add(sendMessage(referencedDtos.get(i), i));
        }

        //giving time for the listener to persist changes
        Thread.sleep(10000);

        verify(listener, times(referencedDtos.size())).listener(any());


        final var listAfterUpdate = getAllStockPrices().toList();

        assertThat(listAfterUpdate)
                .hasSize(referencedDtos.size())
                .doesNotContainAnyElementsOf(referencedDtos)
                .containsExactlyInAnyOrderElementsOf(newDtos);
    }

    private boolean isNotYetUpdated(final List<StockPriceDto> listAfterUpdate, final List<StockPriceDto> referencedDtos) {
        final StockPriceDto updatedDto = listAfterUpdate.get(0);

        final StockPriceDto referencedDto = referencedDtos.stream()
                .filter(dto -> Objects.equals(dto.getStockName(), updatedDto.getStockName()))
                .findFirst().orElseThrow();

        return updatedDto.getPrice().compareTo(referencedDto.getPrice()) == 0;
    }

    private StockPriceDto sendMessage(final StockPriceDto dto, final double newPrice) {
        try {
            final BigDecimal price = BigDecimal.valueOf(newPrice).setScale(2, RoundingMode.CEILING);
            final var event = new StockPriceEvent(dto.getStockName(), price);

            producer.send(new ProducerRecord<>(topic, 0, UUID.randomUUID().toString(), event));

            return new StockPriceDto(dto.getStockName(), price);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
