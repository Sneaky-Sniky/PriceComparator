package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("discounts")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAll() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("/best")
    public ResponseEntity<List<Discount>> getBestDiscounts() {
        return ResponseEntity.ok(this.discountService.getBestDiscounts());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Discount>> getLatestDiscounts() {
        return ResponseEntity.ok(this.discountService.getLatestDiscounts());
    }
}
