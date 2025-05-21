package com.nikoletta.pricecomparator.seed;

import com.nikoletta.pricecomparator.models.Discount;
import com.nikoletta.pricecomparator.models.Product;
import com.nikoletta.pricecomparator.models.ProductPrice;
import com.nikoletta.pricecomparator.repositories.ProductDiscountRepository;
import com.nikoletta.pricecomparator.repositories.ProductPriceRepository;
import com.nikoletta.pricecomparator.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;

@Component
public class ProductSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductDiscountRepository productDiscountRepository;
    public ProductSeeder(ProductRepository productRepository, ProductPriceRepository productPriceRepository, ProductDiscountRepository productDiscountRepository) {
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.productDiscountRepository = productDiscountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(productRepository.count() == 0) {
            seedDataFromCsv();
        }
    }

    private void seedDataFromCsv() throws IOException {
        ClassPathResource dataDir = new ClassPathResource("data");
        File[] files = dataDir.getFile().listFiles((dir, name) -> name.endsWith(".csv"));
        if (files != null) {
            for (File file : files) {
                var name = file.getName();
                var shop = name.split("_")[0];
                if (name.contains("discounts")) {
                    var date = Date.valueOf(name.split("_")[2].replace(".csv", ""));
                    seedDiscounts(file, shop, date);
                }
                else {

                    var date = Date.valueOf(name.split("_")[1].replace(".csv", ""));
                    seedProducts(file, shop, date);
                }
            }
        }
    }

    private void seedProducts(File file, String shop, Date date) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().skip(1)
                    .map(line -> line.split(";"))
                    .map(tokens -> {
                        var p = new ProductPrice();
                        p.setShop(shop);
                        p.setDate(date);
                        var product = new Product();
                        product.setId(tokens[0]);
                        product.setProductName(tokens[1]);
                        product.setProductCategory(tokens[2]);
                        product.setBrand(tokens[3]);
                        product.setPackageQuantity(Double.parseDouble(tokens[4]));
                        product.setPackageUnit(tokens[5]);
                        if(!productRepository.existsById(product.getId())) {
                            productRepository.save(product);
                        }
                        p.setProduct(product);
                        p.setPrice(Double.parseDouble(tokens[6]));
                        p.setCurrency(tokens[7]);
                        return p;
                    })
                    .forEach(productPriceRepository::save);
        }
    }

    private void seedDiscounts(File file, String shop, Date date) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().skip(1)
                    .map(line -> line.split(";"))
                    .map(tokens -> {
                        var d = new Discount();
                        d.setShop(shop);
                        d.setFromDate(Date.valueOf(tokens[6]));
                        d.setToDate(Date.valueOf(tokens[7]));
                        d.setPercentageOfDiscount(Integer.parseInt(tokens[8]));
                        var product = new Product();
                        product.setId(tokens[0]);
                        product.setProductName(tokens[1]);
                        product.setBrand(tokens[2]);
                        product.setPackageUnit(tokens[4]);
                        product.setPackageQuantity(Double.parseDouble(tokens[3]));
                        product.setProductCategory(tokens[5]);


                        if(!productRepository.existsById(product.getId())) {
                            productRepository.save(product);
                        }
                        d.setProduct(product);

                        return d;
                    })
                    .forEach(productDiscountRepository::save);
        }
    }


}
