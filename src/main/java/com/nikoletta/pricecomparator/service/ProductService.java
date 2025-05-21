package com.nikoletta.pricecomparator.service;

import com.nikoletta.pricecomparator.dtos.ProductWithBestPriceDTO;
import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import com.nikoletta.pricecomparator.repositories.ProductDiscountRepository;
import com.nikoletta.pricecomparator.repositories.ProductPriceRepository;
import com.nikoletta.pricecomparator.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductDiscountRepository productDiscountRepository;

    public ProductService(ProductRepository productRepository, ProductPriceRepository productPriceRepository, ProductDiscountRepository productDiscountRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.productDiscountRepository = productDiscountRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    public Map<String, List<ProductWithBestPriceDTO>> getShoppingList(List<String> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        List<ProductPrice> allPrices = productPriceRepository.findByProductIn(products);
        List<Discount> activeDiscounts = productDiscountRepository.findValidDiscounts(products, new Date());

        Map<String, List<Discount>> discountsByProduct = activeDiscounts.stream()
                .collect(Collectors.groupingBy(d -> d.getProduct().getId()));

        Map<String, List<ProductPrice>> pricesByProduct = allPrices.stream()
                .collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        Map<String, List<ProductWithBestPriceDTO>> result = new HashMap<>();

        for (Product product : products) {
            List<ProductPrice> prices = pricesByProduct.getOrDefault(product.getId(), new ArrayList<>());
            List<Discount> discounts = discountsByProduct.getOrDefault(product.getId(), new ArrayList<>());

            Optional<ProductPrice> bestPriceProduct = prices.stream()
                    .min(Comparator.comparingDouble(price -> {
                        OptionalInt maxDiscount = discounts.stream()
                                .mapToInt(Discount::getPercentageOfDiscount)
                                .max();
                        double discountRate = maxDiscount.isPresent() ? maxDiscount.getAsInt() / 100.0 : 0;
                        return price.getPrice() * (1 - discountRate);
                    }));
            bestPriceProduct.ifPresent(productPrice -> {
                       result.putIfAbsent(productPrice.getShop(), new ArrayList<>());
                       result.get(productPrice.getShop()).add(new ProductWithBestPriceDTO(
                               product.getId(),
                               product.getProductName(),
                               productPrice.getPrice()
                       ));
                    });
        }

        return result;

    }
}
