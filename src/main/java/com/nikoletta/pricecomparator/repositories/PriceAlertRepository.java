package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.PriceAlert;
import com.nikoletta.pricecomparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    Optional<PriceAlert> findByProduct(Product product);
}
