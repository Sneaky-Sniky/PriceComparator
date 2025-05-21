package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.dtos.ProductWithBestPriceDTO;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("shopping-list")
    public Map<String, List<ProductWithBestPriceDTO>> getShoppingList(@RequestParam List<String> productIds) {
        return productService.getShoppingList( productIds);
    }
}
