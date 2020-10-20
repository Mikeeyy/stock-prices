package com.matejko.stockprices.persistance.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;


@Table(name = "STOCK_PRICE")
@Entity
@Data
@NoArgsConstructor
@Getter
public class StockPrice {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "STOCK_NAME", nullable = false, unique = true)
    private String stockName;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    public StockPrice(final String stockName, final BigDecimal price) {
        this.stockName = stockName;
        this.price = price;
    }
}
