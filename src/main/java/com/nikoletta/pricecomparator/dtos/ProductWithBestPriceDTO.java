package com.nikoletta.pricecomparator.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithBestPriceDTO {
    private String productId;
    private String productName;
    private Double bestPrice;
}
