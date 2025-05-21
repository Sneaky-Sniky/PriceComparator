package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIdIn(List<String> id);
}
