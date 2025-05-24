package com.nikoletta.pricecomparator.specifications;

import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriceHistorySpecification {
    
    public static Specification<ProductPrice> withFilters(
            String shop,
            String category,
            String brand,
            Date startDate,
            Date endDate) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<ProductPrice, Product> productJoin = root.join("product");
            
            if (shop != null && !shop.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("shop"), shop));
            }
            
            if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(productJoin.get("productCategory"), category));
            }
            
            if (brand != null && !brand.isEmpty()) {
                predicates.add(criteriaBuilder.equal(productJoin.get("brand"), brand));
            }
            
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            }
            
            query.orderBy(criteriaBuilder.asc(root.get("date")));
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 