package com.nikoletta.pricecomparator.service;

import com.nikoletta.pricecomparator.dtos.PriceHistoryDTO;
import com.nikoletta.pricecomparator.dtos.ProductAlternativeDTO;
import com.nikoletta.pricecomparator.dtos.ProductWithBestPriceDTO;
import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import com.nikoletta.pricecomparator.repositories.ProductDiscountRepository;
import com.nikoletta.pricecomparator.repositories.ProductPriceRepository;
import com.nikoletta.pricecomparator.repositories.ProductRepository;
import com.nikoletta.pricecomparator.specifications.PriceHistorySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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

    public List<ProductAlternativeDTO> findBetterAlternatives(String productId) {
        Product targetProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        // Get latest prices for the target product
        List<ProductPrice> targetPrices = productPriceRepository.findLatestPricesByProduct(targetProduct, new Date());
        if (targetPrices.isEmpty()) {
            return Collections.emptyList();
        }

        // Calculate the best unit price for the target product
        double bestTargetUnitPrice = targetPrices.stream()
                .mapToDouble(price -> price.getPrice() / targetProduct.getPackageQuantity())
                .min()
                .orElse(Double.MAX_VALUE);

        // Find products in the same category
        List<Product> sameCategoryProducts = productRepository.findAll().stream()
                .filter(p -> p.getProductCategory().equals(targetProduct.getProductCategory()))
                .filter(p -> !p.getId().equals(productId))
                .collect(Collectors.toList());

        // Get latest prices for all alternative products
        List<ProductPrice> alternativePrices = productPriceRepository.findByProductIn(sameCategoryProducts);

        // Group prices by product
        Map<String, List<ProductPrice>> pricesByProduct = alternativePrices.stream()
                .collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        // Find products with better unit prices
        return sameCategoryProducts.stream()
                .map(product -> {
                    List<ProductPrice> productPrices = pricesByProduct.getOrDefault(product.getId(), Collections.emptyList());
                    if (productPrices.isEmpty()) return null;

                    Optional<ProductPrice> bestPrice = productPrices.stream()
                            .min(Comparator.comparingDouble(price -> price.getPrice() / product.getPackageQuantity()));

                    return bestPrice.map(price -> {
                        double unitPrice = price.getPrice() / product.getPackageQuantity();
                        if (unitPrice < bestTargetUnitPrice) {
                            return ProductAlternativeDTO.fromProduct(product, price.getPrice(), price.getShop());
                        }
                        return null;
                    }).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Map<String, List<PriceHistoryDTO>> getPriceHistory(String shop, String category, String brand, Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            startDate = Date.from(LocalDate.now().minusMonths(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Specification<ProductPrice> spec = PriceHistorySpecification.withFilters(
                shop, category, brand, startDate, endDate);

        List<ProductPrice> priceHistory = productPriceRepository.findAll(spec);

        return priceHistory.stream()
                .map(PriceHistoryDTO::fromProductPrice)
                .collect(Collectors.groupingBy(PriceHistoryDTO::getProductId));
    }
}
