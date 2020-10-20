package com.matejko.stockprices.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
public class EndpointTest extends BaseEndpointApiHelper {

    @Test
    @DisplayName("Get all should return status 200 and all of the persisted stock prices")
    public void testGetAll() throws Exception {
        final var prices = getAllStockPrices().toList();

        assertThat(prices)
                .hasSize(5)
                .doesNotContainNull()
                .containsExactlyInAnyOrderElementsOf(storedDtos());
    }


    @Test
    @DisplayName("Get by name should return status 200 and data of persisted stock price for stored stock")
    public void testGetByName() {
        storedDtos().forEach(dto -> {
            try {
                final var stockPrice = getStockPrice(dto.getStockName());

                assertThat(stockPrice)
                        .isNotNull()
                        .isEqualTo(dto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("Get by name should return status 204 for not stored stock price")
    public void testGetByNameFailure() throws Exception {
        final String notStoredDtoName = UUID.randomUUID().toString();

        mockMvc.perform(get("/stock-prices/" + notStoredDtoName))
                .andExpect(status().is(204));
    }


}
