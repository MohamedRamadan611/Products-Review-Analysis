package com.product.productreviewanalysis.service.Product;

import com.product.productreviewanalysis.dto.product.ProductEntityToDtoUpdate;
import com.product.productreviewanalysis.dto.product.ProductDtoToEntity;
import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product addProduct(ProductDtoToEntity productDtoToEntity) {
        Product product = Product.builder()
                .name(productDtoToEntity.getName())
                .price(productDtoToEntity.getPrice())
                .quantity(productDtoToEntity.getQuantity())
                .feedbacks(productDtoToEntity.getFeedbacks())
                .category(productDtoToEntity.getCategory())
                .rate(productDtoToEntity.getRate())
                .build();

        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int productId)
    {
         return productRepository.findById(productId).orElseThrow(() -> new UserNotFoundException("Product is Not Found"));
    }

    @Override
    public List<Product> getProductsByCategory(String category) {

        return productRepository.findAllByCategory(category);
    }

    @Override
    public List<Product> getTwoProductToCompare(int firstProduct, int secondProduct) {
        Product product = productRepository.findById(firstProduct).orElseThrow(() -> new UserNotFoundException("Your First Product is Not Found"));
        Product product2 = productRepository.findById(secondProduct).orElseThrow(() -> new UserNotFoundException("Your Second Product is Not Found"));
        return productRepository.findAllById(List.of(firstProduct, secondProduct));
    }

    @Override
    public Product getProductByName(String name){
        return productRepository.findByName(name).stream().findAny().orElseThrow(() -> new UserNotFoundException("Your Product is Not Found"));
    }

    @Override
    public String deleteProductById(int productID){
        Product product = productRepository.findById(productID).orElseThrow(() -> new UserNotFoundException("Product is Not Found"));
        productRepository.deleteById(productID);
        return "Your Product is Deleted";
    }

    @Override
    @Transactional
    public String updateProductByID(ProductEntityToDtoUpdate productDto, int productId){
            Product product = productRepository.findById(productId).orElseThrow(() -> new UserNotFoundException("Product is Not Found"));

            product.setProductId(productId);
            if(valid(productDto.getName()))
                product.setName(productDto.getName());
            if(valid(productDto.getQuantity()))
                product.setQuantity(productDto.getQuantity());
            if(valid(productDto.getPrice()))
                product.setPrice(productDto.getPrice());
            if(valid(productDto.getCategory()))
                product.setCategory(productDto.getCategory());
            if(valid(productDto.getFeedbacks()))
                product.setFeedbacks(productDto.getFeedbacks());
            if(valid(productDto.getRate()))
                product.setRate(productDto.getRate());

        productRepository.save(product);
        return "Your Product is updated";
    }

    private boolean valid(Object obj)
    {
        return !ObjectUtils.isEmpty(obj);
    }
    @Override
    @Transactional
    public String updateNameByName(String name, String nameUpdated){
        Product product = productRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("Product is Not Found"));

        if(!nameUpdated.equalsIgnoreCase(product.getName()))
        {
            product.setName(nameUpdated);
            productRepository.save(product);
        }
        else
        {
            return "You aren't doing any update";
        }
        return "Your Product is updated";
    }
    @Override
    @Transactional
    public String updateProductFeedbackByID(int id, String feedback) {
        Product product = productRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Your Product is Not Fount"));

        product.setFeedbacks(product.getFeedbacks() + " , " + feedback);
        productRepository.save(product);

        return "Your Product is updated";
    }
    @Override
    @Transactional
    public String updateProductRateByID(int id, int rate) {
        Product product = productRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Your Product is Not Fount"));
        if(product.getRate() != rate)
        {
            product.setRate(rate);
            productRepository.save(product);
        }
        else
        {
            return "You aren't doing any update";
        }
        return "Your Product is updated";
    }

    @Override
    public List<String> getAllFeedbacks() {

        List<String> listOfFeedbacks = productRepository.getAllFeatures();
        //listOfFeedbacks.add(0,"Product_ID - ProductName - Feedbacks" );
        return listOfFeedbacks;
    }
}
