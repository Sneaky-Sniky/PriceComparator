package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductDiscountRepository extends JpaRepository<Discount, String> {
    List<Discount> findByFromDateGreaterThanEqual(Date fromDate);
    @Query("SELECT d FROM Discount d WHERE d.product IN :products AND :now BETWEEN d.fromDate AND d.toDate")
    List<Discount> findValidDiscounts(@Param("products") List<Product> products, @Param("now") Date now);
}
