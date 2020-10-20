package com.matejko.stockprices.api;

import com.matejko.stockprices.service.StockPriceService;
import com.matejko.stockprices.shared.dto.StockPriceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stock-prices", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(description = "Set of endpoints used for retrieving stock prices")
public class StockPriceController {
    private final StockPriceService stockPriceService;

    @GetMapping
    @ApiOperation("Retrieves a list of all stock prices by given pagination")
    public Page<StockPriceDto> pageableFindAll(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") final int pageNumber,
                                               @RequestParam(value = "pageSize", defaultValue = "0") final int pageSize) {
        final var pagination = pageSize == 0 ? Pageable.unpaged() : PageRequest.of(pageNumber, pageSize);

        return stockPriceService.pageableFindAll(pagination);
    }

    @GetMapping("/{stockName}")
    @ApiOperation("Retrieves stock price by stock name")
    @ApiResponses({
            @ApiResponse(message = "Stock price retrieved", code = 200),
            @ApiResponse(message = "Stock price could not be found", code = 204)
    })
    public ResponseEntity<StockPriceDto> findByStockName(@PathVariable final String stockName) {
        return stockPriceService.findByStockName(stockName)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.noContent()::build);
    }
}
