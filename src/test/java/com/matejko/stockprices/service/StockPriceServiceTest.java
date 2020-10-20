package com.matejko.stockprices.service;

import com.matejko.stockprices.persistance.entity.StockPrice;
import com.matejko.stockprices.persistance.repository.StockPriceRepository;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import com.matejko.stockprices.shared.mapper.StockPriceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StockPriceServiceTest {
    private StockPriceService stockPriceService;

    private StockPriceRepository repository;
    private StockPriceMapper mapper;

    @BeforeEach
    void setUp() {
        repository = mock(StockPriceRepository.class);
        mapper = spy(Mappers.getMapper(StockPriceMapper.class));

        stockPriceService = new StockPriceService(repository, mapper);
    }

    @DisplayName("Should return all dtos")
    @Test
    void pageableFindAll() {
        final var listOfStockPrices = getListOfStockPrices();

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(listOfStockPrices));

        final var result = stockPriceService.pageableFindAll(Pageable.unpaged()).toList();

        assertThat(result)
                .hasSize(listOfStockPrices.size());

        result.forEach(dto -> assertEquality(findEntity(listOfStockPrices, dto.getStockName()), dto));
    }

    @DisplayName("Should return empty optional for not found stock name")
    @Test
    void findByStockNameEmptyOptional() {
        when(repository.findByStockName(anyString())).thenReturn(Optional.empty());

        final var stockName = UUID.randomUUID().toString();

        final var dto = stockPriceService.findByStockName(stockName);

        assertThat(dto)
                .isEmpty();
    }

    @DisplayName("Should return converted dto for found stock name")
    @Test
    void findByStockNamePresentOptional() {
        final var entity = newStockPrice();

        when(repository.findByStockName(entity.getStockName())).thenReturn(Optional.of(entity));

        final var dto = stockPriceService.findByStockName(entity.getStockName());

        assertThat(dto)
                .isPresent();

        assertEquality(entity, dto.get());
    }

    @Test
    void getAllStockNames() {
        final var listOfStockPrices = getListOfStockPrices();

        when(repository.findAll()).thenReturn(listOfStockPrices);

        final var result = stockPriceService.getAllStockNames();

        assertThat(result)
                .hasSize(listOfStockPrices.size());

        result.forEach(name -> assertThat(findEntity(listOfStockPrices, name)).isNotNull());
    }

    @DisplayName("Should create a new stock price if not found")
    @Test
    void updateStockPriceNew() {
        final var priceDto = newStockPriceDto();

        when(repository.findByStockName(priceDto.getStockName())).thenReturn(Optional.empty());

        stockPriceService.updateStockPrice(priceDto);

        verify(repository, times(1)).findByStockName(priceDto.getStockName());
        verify(mapper, times(1)).toEntity(any());
        verify(repository, times(1)).save(any());
    }

    @DisplayName("Should update stock price if found")
    @Test
    void updateStockPriceUpdate() {
        final var priceDto = newStockPriceDto();

        when(repository.findByStockName(priceDto.getStockName())).thenReturn(Optional.of(mock(StockPrice.class)));

        stockPriceService.updateStockPrice(priceDto);

        verify(repository, times(1)).findByStockName(priceDto.getStockName());
        verify(mapper, never()).toEntity(any());
        verify(repository, times(1)).save(any());
    }

    private List<StockPrice> getListOfStockPrices() {
        return IntStream.range(0, 100)
                .mapToObj(i -> newStockPrice())
                .collect(Collectors.toUnmodifiableList());
    }

    private StockPrice newStockPrice() {
        final var stockPrice = mock(StockPrice.class);
        when(stockPrice.getId()).thenReturn(UUID.randomUUID().toString());
        when(stockPrice.getPrice()).thenReturn(BigDecimal.valueOf(new Random().nextInt(1500)));
        when(stockPrice.getStockName()).thenReturn(UUID.randomUUID().toString());
        return stockPrice;
    }

    private StockPriceDto newStockPriceDto() {
        return new StockPriceDto(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(new Random().nextInt(1500))
        );
    }

    private StockPrice findEntity(final List<StockPrice> listOfStockPrices, final String stockName) {
        return listOfStockPrices.stream()
                .filter(entity -> Objects.equals(entity.getStockName(), stockName))
                .findFirst()
                .orElseThrow();
    }

    private void assertEquality(final StockPrice entity, final StockPriceDto dto) {
        assertThat(List.of(entity, dto))
                .doesNotContainNull();

        assertThat(entity)
                .extracting(StockPrice::getPrice, StockPrice::getStockName)
                .containsExactly(dto.getPrice(), dto.getStockName());
    }

}