package com.nikoletta.pricecomparator.dtos;

import com.nikoletta.pricecomparator.models.Product;
import lombok.Data;

@Data
public class ProductAlternativeDTO {
    private String id;
    private String productName;
    private String productCategory;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
    private double bestPrice;
    private String shop;
    private double unitPrice;

    public static ProductAlternativeDTO fromProduct(Product product, double bestPrice, String shop) {
        ProductAlternativeDTO dto = new ProductAlternativeDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductCategory(product.getProductCategory());
        dto.setBrand(product.getBrand());
        dto.setPackageQuantity(product.getPackageQuantity());
        dto.setPackageUnit(product.getPackageUnit());
        dto.setBestPrice(bestPrice);
        dto.setShop(shop);
        dto.setUnitPrice(bestPrice / product.getPackageQuantity());
        return dto;
    }
} 