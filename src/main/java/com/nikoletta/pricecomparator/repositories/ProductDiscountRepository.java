package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ProductDiscountRepository extends JpaRepository<Discount, String> {
    List<Discount> findByFromDateGreaterThanEqual(Date fromDate);
}
