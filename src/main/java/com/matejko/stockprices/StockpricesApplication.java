package com.matejko.stockprices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableScheduling
public class StockpricesApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockpricesApplication.class, args);
	}

}
