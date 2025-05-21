package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.service.DiscountService;
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
    public List<Discount> getAll() {
        return discountService.findAll();
    }

    @GetMapping("/best")
    public List<Discount> getBestDiscounts() {
        return this.discountService.getBestDiscounts();
    }

    @GetMapping("/latest")
    public List<Discount> getLatestDiscounts() {
        return this.discountService.getLatestDiscounts();
    }
}
