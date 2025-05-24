package com.nikoletta.pricecomparator.dtos;

import com.nikoletta.pricecomparator.models.ProductPrice;
import lombok.Data;

import java.util.Date;

@Data
public class PriceHistoryDTO {
    private String productId;
    private String productName;
    private String brand;
    private String productCategory;
    private String shop;
    private double price;
    private Date date;
    private double packageQuantity;
    private String packageUnit;
    private double unitPrice;

    public static PriceHistoryDTO fromProductPrice(ProductPrice price) {
        PriceHistoryDTO dto = new PriceHistoryDTO();
        dto.setProductId(price.getProduct().getId());
        dto.setProductName(price.getProduct().getProductName());
        dto.setBrand(price.getProduct().getBrand());
        dto.setProductCategory(price.getProduct().getProductCategory());
        dto.setShop(price.getShop());
        dto.setPrice(price.getPrice());
        dto.setDate(price.getDate());
        dto.setPackageQuantity(price.getProduct().getPackageQuantity());
        dto.setPackageUnit(price.getProduct().getPackageUnit());
        dto.setUnitPrice(price.getPrice() / price.getProduct().getPackageQuantity());
        return dto;
    }
} 