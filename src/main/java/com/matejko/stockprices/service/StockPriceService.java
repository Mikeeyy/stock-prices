package com.matejko.stockprices.service;

import com.matejko.stockprices.persistance.entity.StockPrice;
import com.matejko.stockprices.persistance.repository.StockPriceRepository;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import com.matejko.stockprices.shared.mapper.StockPriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockPriceService {
    private final StockPriceRepository stockPriceRepository;
    private final StockPriceMapper stockPriceMapper;

    public Page<StockPriceDto> pageableFindAll(final Pageable pageable) {
        return stockPriceRepository.findAll(pageable)
                .map(stockPriceMapper::toDto);
    }

    public Optional<StockPriceDto> findByStockName(final String stockName) {
        return stockPriceRepository.findByStockName(stockName)
                .map(stockPriceMapper::toDto);
    }

    @Cacheable("stockNames")
    public List<String> getAllStockNames() {
        return stockPriceRepository.findAll()
                .stream()
                .map(StockPrice::getStockName)
                .collect(Collectors.toUnmodifiableList());
    }

    public void updateStockPrice(final StockPriceDto dto) {
        final var stockPrice = stockPriceRepository.findByStockName(dto.getStockName())
                .map(entity -> updateEntity(entity, dto))
                .orElseGet(() -> stockPriceMapper.toEntity(dto));

        stockPriceRepository.save(stockPrice);
    }

    private StockPrice updateEntity(final StockPrice entity, final StockPriceDto dto) {
        entity.setPrice(dto.getPrice());
        entity.setStockName(dto.getStockName());
        return entity;
    }
}
