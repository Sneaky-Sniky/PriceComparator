package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductIn(List<Product> products);
}
