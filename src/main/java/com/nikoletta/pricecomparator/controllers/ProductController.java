package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.dtos.PriceHistoryDTO;
import com.nikoletta.pricecomparator.dtos.ProductAlternativeDTO;
import com.nikoletta.pricecomparator.dtos.ProductWithBestPriceDTO;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        return productService.getShoppingList(productIds);
    }

    @GetMapping("alternatives")
    public List<ProductAlternativeDTO> getBetterAlternatives(@RequestParam String productId) {
        return productService.findBetterAlternatives(productId);
    }

    @GetMapping("price-history")
    public Map<String, List<PriceHistoryDTO>> getPriceHistory(
            @RequestParam(required = false) String shop,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return productService.getPriceHistory(shop, category, brand, startDate, endDate);
    }
}
