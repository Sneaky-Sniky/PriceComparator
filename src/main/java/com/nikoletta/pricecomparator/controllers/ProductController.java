package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.dtos.PriceHistoryDTO;
import com.nikoletta.pricecomparator.dtos.ProductAlternativeDTO;
import com.nikoletta.pricecomparator.dtos.ProductWithBestPriceDTO;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("shopping-list")
    public ResponseEntity<Map<String, List<ProductWithBestPriceDTO>>> getShoppingList(@RequestParam List<String> productIds) {
        return ResponseEntity.ok(productService.getShoppingList(productIds));
    }

    @GetMapping("alternatives")
    public ResponseEntity<List<ProductAlternativeDTO>> getBetterAlternatives(@RequestParam String productId) {
        return ResponseEntity.ok(productService.findBetterAlternatives(productId));
    }

    @GetMapping("price-history")
    public ResponseEntity<Map<String, List<PriceHistoryDTO>>> getPriceHistory(
            @RequestParam(required = false) String shop,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(productService.getPriceHistory(shop, category, brand, startDate, endDate));
    }
}
