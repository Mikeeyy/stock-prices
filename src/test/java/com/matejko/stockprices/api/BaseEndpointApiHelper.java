package com.matejko.stockprices.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseEndpointApiHelper {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Page<StockPriceDto> getAllStockPrices() throws Exception {
        final var content = mockMvc.perform(get("/stock-prices"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, StockPriceDtoPage.class);
    }

    protected StockPriceDto getStockPrice(final String stockName) throws Exception {
        final var content = mockMvc.perform(get("/stock-prices/" + stockName))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(content, StockPriceDto.class);
    }

    protected Iterable<? extends StockPriceDto> storedDtos() {
        return List.of(
                new StockPriceDto("Apple", valueOf(114.83)),
                new StockPriceDto("Microsoft", valueOf(208.05)),
                new StockPriceDto("IG Group", new BigDecimal("801.00")),
                new StockPriceDto("Tesla", valueOf(416.83)),
                new StockPriceDto("General Motors", valueOf(31.28))
        );
    }
}
