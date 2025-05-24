package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductIn(List<Product> products);
    List<ProductPrice> findAllByProduct(Product product);
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product = :product AND pp.date <= :date AND (pp.date, pp.shop) IN (SELECT MAX(p.date), p.shop FROM ProductPrice p WHERE p.product = :product AND p.date <= :date GROUP BY p.shop)")
    List<ProductPrice> findLatestPricesByProduct(Product product, Date date);
}
