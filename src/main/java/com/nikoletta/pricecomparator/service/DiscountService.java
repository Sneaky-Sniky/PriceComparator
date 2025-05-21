package com.nikoletta.pricecomparator.service;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.repositories.ProductDiscountRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {
    private final ProductDiscountRepository productDiscountRepository;

    public DiscountService(ProductDiscountRepository productDiscountRepository) {
        this.productDiscountRepository = productDiscountRepository;
    }

    public List<Discount> findAll() {
        return productDiscountRepository.findAll();
    }

    public List<Discount> getBestDiscounts() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("percentageOfDiscount").descending());
        return productDiscountRepository.findAll(pageRequest).getContent();
    }
}