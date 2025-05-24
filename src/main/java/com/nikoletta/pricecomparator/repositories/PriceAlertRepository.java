package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.PriceAlert;
import com.nikoletta.pricecomparator.models.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("jpa")
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    Optional<PriceAlert> findByProduct(Product product);
}
