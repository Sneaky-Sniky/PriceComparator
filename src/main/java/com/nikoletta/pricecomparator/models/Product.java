package com.nikoletta.pricecomparator.models;

import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Product implements Serializable {
    @Id
    private String id;
    private String productName;
    private String productCategory;
    private String brand;
    private double packageQuantity;
    private String packageUnit;

}
