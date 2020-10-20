package com.matejko.stockprices.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StockPriceEvent {
    private String stockName;
    private BigDecimal price;
}
