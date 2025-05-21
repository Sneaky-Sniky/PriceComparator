package com.nikoletta.pricecomparator.repositories;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDiscountRepository extends JpaRepository<Discount, String> {

}
