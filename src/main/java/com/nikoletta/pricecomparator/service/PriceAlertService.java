package com.nikoletta.pricecomparator.service;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.PriceAlert;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import com.nikoletta.pricecomparator.repositories.PriceAlertRepository;
import com.nikoletta.pricecomparator.repositories.ProductDiscountRepository;
import com.nikoletta.pricecomparator.repositories.ProductPriceRepository;
import com.nikoletta.pricecomparator.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PriceAlertService {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductDiscountRepository productDiscountRepository;
    private final PriceAlertRepository priceAlertRepository;

    public PriceAlertService(ProductRepository productRepository,
                             ProductPriceRepository productPriceRepository,
                             ProductDiscountRepository productDiscountRepository, PriceAlertRepository priceAlertRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.productDiscountRepository = productDiscountRepository;
        this.priceAlertRepository = priceAlertRepository;
    }

    public PriceAlert createPriceAlert(String productId, double targetPrice) {
        // Verify product exists
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        PriceAlert alert;
        var oldPriceAlert = priceAlertRepository.findByProduct(product.get());
        alert = oldPriceAlert.orElseGet(PriceAlert::new);
        alert.setProduct(product.get());
        alert.setTargetPrice(targetPrice);
        priceAlertRepository.save(alert);
        return alert;
    }

    public List<PriceAlert> getActiveAlerts() {
        return priceAlertRepository.findAll();
    }

    @Transactional
    public void checkPriceAlerts() {
        var priceAlerts = priceAlertRepository.findAll();
        for (PriceAlert alert : priceAlerts) {
            Product product = alert.getProduct();
            if (product != null) {
                List<ProductPrice> productPrices = productPriceRepository.findLatestPricesByProduct(product, new Date());
                List<Discount> activeDiscounts = productDiscountRepository.findValidDiscounts(
                        Collections.singletonList(product),
                        new Date()
                );
                for(var productPrice : productPrices) {
                    double bestPrice = productPrice.getPrice();
                    if (!activeDiscounts.isEmpty()) {
                        int maxDiscount = activeDiscounts.stream().filter(d -> d.getShop().equals(productPrice.getShop()))
                                .mapToInt(Discount::getPercentageOfDiscount)
                                .max()
                                .orElse(0);
                        bestPrice = productPrice.getPrice() * (1 - maxDiscount / 100.0);
                    }

                    if (bestPrice <= alert.getTargetPrice()) {
                        notifyPriceAlert(alert, product, bestPrice, productPrice.getShop());
                    }
                }
            }
        }
    }

    private void notifyPriceAlert(PriceAlert alert, Product product, double currentPrice, String shop) {
        // TODO: Implement notification logic (email, push notification, etc.)
        System.out.println("Price Alert: Product " + product.getProductName() +
                          " has reached target price of " + alert.getTargetPrice() +
                          " (Current price: " + currentPrice + " at " + shop + ")");
    }

    public void deleteAlert(Long alertId) {
        priceAlertRepository.deleteById(alertId);
    }
} 