package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Profile("jpa")
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long>, JpaSpecificationExecutor<ProductPrice> {
    List<ProductPrice> findByProductIn(List<Product> products);
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product = :product AND pp.date <= :date AND (pp.date, pp.shop) IN (SELECT MAX(p.date), p.shop FROM ProductPrice p WHERE p.product = :product AND p.date <= :date GROUP BY p.shop)")
    List<ProductPrice> findLatestPricesByProduct(Product product, Date date);
}
