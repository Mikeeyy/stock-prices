package com.matejko.stockprices.shared.mapper;

import com.matejko.stockprices.messaging.event.StockPriceEvent;
import com.matejko.stockprices.persistance.entity.StockPrice;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockPriceMapper {
    StockPriceDto toDto(final StockPrice entity);

    StockPriceDto toDto(final StockPriceEvent event);

    StockPrice toEntity(final StockPriceDto dto);

    StockPriceEvent toEvent(final StockPriceDto dto);
}
