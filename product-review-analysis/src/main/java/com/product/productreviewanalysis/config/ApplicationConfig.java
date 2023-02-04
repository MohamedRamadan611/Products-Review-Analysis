package com.product.productreviewanalysis.config;

import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class ApplicationConfig {
    @Autowired
    private ProductRepository productRepository;
    @Bean
    public CommandLineRunner commandLineRunner ()
    {
        return  args -> {
            List<Product> products = IntStream.rangeClosed(1,10)
                    .mapToObj(product -> Product.builder()
                            .name(List.of("T-shirt" , "Jacket", "Shoes" , "SweetPants").get(new Random().nextInt(4))+ " " + product)
                            .price(new Random().nextInt(2500))
                            .quantity(new Random().nextInt(20))
                            .category(List.of("Sport" , " Casual" , "Classic").get(new Random().nextInt(3)))
                            .feedbacks((List.of("Good", "Bad", "Professional").get(new Random().nextInt(2))))
                            .rate(new Random().nextInt(5))
                            .build()).collect(Collectors.toList());

                productRepository.saveAll(products);
        };
    }
}
