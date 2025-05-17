package com.nikoletta.pricecomparator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PriceComparatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceComparatorApplication.class, args);
    }

}
