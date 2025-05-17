package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
