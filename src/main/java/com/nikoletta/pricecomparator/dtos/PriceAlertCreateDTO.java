package com.nikoletta.pricecomparator.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriceAlertCreateDTO implements Serializable {
    private String productId;
    private Double targetPrice;
}
