package com.product.productreviewanalysis.controller;

import com.product.productreviewanalysis.dto.product.ProductDtoToEntity;
import com.product.productreviewanalysis.dto.product.ProductEntityToDtoUpdate;
import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.service.Product.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/welcome")
    public String getWelcome()
    {
        return "<h1>Hello In Product System Analysis</h1>";
    }

    @PostMapping("/addproduct")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid ProductDtoToEntity productDtoToEntity)
    {
        ResponseEntity<Product> product = new ResponseEntity<Product>(productService.addProduct(productDtoToEntity) ,HttpStatus.CREATED);
        logger.info("Admin in Add Product Page");
        return product;
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String,List<Product>>> getAllProducts()
    {
        logger.info("Admin in View Products Page");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/allfeedback")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<String>> getAllFeedbacks()
    {
        logger.info("Admin in View Feedbacks of Products Page");
        return ResponseEntity.ok(productService.getAllFeedbacks());
    }
    @GetMapping ("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<Product> getProductByID(@PathVariable int productId){
        logger.info("You Are in Get Product by Id Page");
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/productname")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<Product> getProductByName(@RequestParam String name){
        logger.info("You're in Get Product by Name Page");
        return ResponseEntity.ok(productService.getProductByName(name));
    }
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteProductByID(@PathVariable ("productId") int productID){
        logger.info("Admin in Delete Product by Id Page");
        return ResponseEntity.ok(productService.deleteProductById(productID));
    }
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public ResponseEntity<String> updateProductByID (@RequestBody ProductEntityToDtoUpdate productEntityToDtoUpdate, @PathVariable int productId){
        logger.info("Admin in Update Product by Id Page");
        return ResponseEntity.ok(productService.updateProductByID(productEntityToDtoUpdate, productId));
    }

    @PutMapping("/updatename")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> updateProductNameByName(@RequestParam String name , @RequestParam String nameUpdated )
    {
        logger.info("Admin in Update Product Name by Name Page");
        return ResponseEntity.ok(productService.updateNameByName(name , nameUpdated));
    }

    @PutMapping("/updatefeedback")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<String> updateProductFeedbackById(@RequestParam int id , @RequestParam(name = "feedbacks") String feedback)
    {
        logger.info("You're in Update Product Feedback by ID Page");
        return ResponseEntity.ok(productService.updateProductFeedbackByID(id , feedback));
    }

    @PutMapping("/updaterate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<String> updateProductRateById(@RequestParam int id , @RequestParam int rate)
    {
        logger.info("You're in Update Product Rate by ID Page");
        return ResponseEntity.ok(productService.updateProductRateByID(id , rate));
    }

    @GetMapping("/compare")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<List<Product>> getTwoProductToCompare(@RequestParam  int firstProduct ,@RequestParam  int secondProduct) {
        logger.info("You're in Compare Two Products by Id Page");
        return ResponseEntity.ok(productService.getTwoProductToCompare(firstProduct,secondProduct));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_USER')")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category)
    {
        logger.info("You're in Get All Category of Product by Category Name Page");
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

}
