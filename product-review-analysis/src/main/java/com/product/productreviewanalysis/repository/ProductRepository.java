package com.product.productreviewanalysis.repository;

import com.product.productreviewanalysis.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findAllByCategory(String category);

    Optional<Product> findByName(String name);

    @Query(value = "select product_id , name , feedbacks from product p" , nativeQuery = true)
    List<String> getAllFeatures();
}
