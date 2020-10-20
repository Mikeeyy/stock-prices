package com.matejko.stockprices.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring cannot deserialize Page<StockPriceDto> by default
 */
public class StockPriceDtoPage extends PageImpl<StockPriceDto> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StockPriceDtoPage(@JsonProperty("content") List<StockPriceDto> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") Long totalElements,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("totalPages") int totalPages,
                             @JsonProperty("sort") JsonNode sort,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public StockPriceDtoPage(List<StockPriceDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public StockPriceDtoPage(List<StockPriceDto> content) {
        super(content);
    }

    public StockPriceDtoPage() {
        super(new ArrayList<>());
    }
}