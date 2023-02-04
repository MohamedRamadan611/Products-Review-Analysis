package com.product.productreviewanalysis.repository;

import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    @BeforeEach
    void setUp() {

        Product product = Product.builder()
                .productId(1)
                .name("Jacket")
                .price(250)
                .quantity(15)
                .rate(3)
                .category("Sport")
                .feedbacks("Good")
                .build();

        testEntityManager.merge(product);
    }

    @Test
    @DisplayName("Get Products By Category")
    void findAllByCategory() {

        List<Product> productList = productRepository.findAllByCategory("Sport");

        assertEquals(productList.get(0).getCategory() , "Sport");
    }

    @Test
    @DisplayName("Update Product Name By ID")
    void updateProductName() {
        Product product = productRepository.findById(1).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        product = Product.builder().productId(product.getProductId()).name("Shirt").build();
        productRepository.save(product);
    }

    @Test
    @DisplayName("Update Product Name By Name")
    void updateProductNameByName() {
        Product product = productRepository.findByName("Jacket").orElseThrow(() -> new UserNotFoundException("Not Found"));
        product = Product.builder()
                .productId(product.getProductId())
                .name("T-shirt")
                .build();
        productRepository.save(product);
    }

    @Test
    @DisplayName("Get All Features")
    void getAllFeatures() {
        List<String> list = productRepository.getAllFeatures();

        assertEquals(list.get(0) , "1,Jacket,Good");
    }
}