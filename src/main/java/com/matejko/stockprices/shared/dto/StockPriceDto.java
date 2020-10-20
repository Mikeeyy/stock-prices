package com.matejko.stockprices.shared.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@ApiModel("Desribes stock price")
public class StockPriceDto {
    @ApiModelProperty("Name of the stock")
    String stockName;

    @ApiModelProperty("Price of the stock")
    BigDecimal price;
}
