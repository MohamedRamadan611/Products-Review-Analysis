package com.product.productreviewanalysis.service.Product;

import com.product.productreviewanalysis.config.Admin.AdminAndUserInfoDetailsService;
import com.product.productreviewanalysis.config.SecurityConfig;
import com.product.productreviewanalysis.controller.AdminController;
import com.product.productreviewanalysis.controller.UserController;
import com.product.productreviewanalysis.dto.product.ProductDtoToEntity;
import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.event.listener.RegisterCompleteEventListener;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.repository.ProductRepository;
import com.product.productreviewanalysis.repository.admin.AdminPasswordTokenRepository;
import com.product.productreviewanalysis.repository.admin.AdminRepository;
import com.product.productreviewanalysis.repository.admin.AdminVerificationTokenRepository;
import com.product.productreviewanalysis.repository.user.UserPasswordTokenRepository;
import com.product.productreviewanalysis.repository.user.UserRepository;
import com.product.productreviewanalysis.repository.user.UserVerificationTokenRepository;
import com.product.productreviewanalysis.service.Admin.AdminService;
import com.product.productreviewanalysis.service.User.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
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

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        Mockito.when(productRepository.findAllByCategory("Sport")).thenReturn(List.of(product));

        Mockito.when(productRepository.findByName("Jacket")).thenReturn(Optional.ofNullable(product));

       // testEntityManager.persist(product);
    }

    @Test
    @DisplayName("Add New Product")
    void addProduct() {
        ProductDtoToEntity productDtoToEntity = ProductDtoToEntity.builder()
                .name("Jacket")
                .price(250)
                .quantity(15)
                .rate(3)
                .category("Sport")
                .feedbacks("Good")
                .build();

        productService.addProduct(productDtoToEntity);
    }

    @Test
    @DisplayName("Get All Products")
    void getAllProducts() {

        Map<String,List<Product>> list = productService.getAllProducts();
        assertEquals(list.get("Sport").get(0).getName(),"Jacket");
    }

    @Test
    @DisplayName("Get All Products in same Category")
    void getProductsByCategory() {
        List<Product> products = productService.getProductsByCategory("Sport");
        assertEquals(products.get(0).getName(),"Jacket");
    }
    @Test
    void deleteProductById() throws UserNotFoundException {
        String result = productService.deleteProductById(1);
    }

    @Test
    @DisplayName("Update Product name by name")
    void updateNameByName() throws UserNotFoundException {
        Product product = productRepository.findByName("Jacket").get();
        if(product.equals(Optional.empty()))
            throw new UsernameNotFoundException("Your Product is not found");

        productService.updateNameByName("Jacket", "T-shirt");
    }
}