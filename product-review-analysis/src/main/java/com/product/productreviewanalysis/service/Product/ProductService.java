package com.product.productreviewanalysis.service.Product;

import com.product.productreviewanalysis.dto.product.ProductEntityToDtoUpdate;
import com.product.productreviewanalysis.dto.product.ProductDtoToEntity;
import com.product.productreviewanalysis.entity.Product;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;

import java.util.List;

public interface ProductService {
    Product addProduct(ProductDtoToEntity productDtoToEntity);

    List<Product> getAllProducts();

    Product getProductById(int productId) throws UserNotFoundException;

    List<Product> getProductsByCategory(String category);

    List<Product> getTwoProductToCompare(int firstProduct, int secondProduct) throws UserNotFoundException;

    Product getProductByName(String name) throws UserNotFoundException;

    String deleteProductById(int productID) throws UserNotFoundException;

    String updateProductByID(ProductEntityToDtoUpdate productEntityToDtoUpdate, int productId) throws UserNotFoundException;

    String updateNameByName(String name, String nameUpdated) throws UserNotFoundException;

    String updateProductFeedbackByID(int id, String feedback);

    public String updateProductRateByID(int id, int rate);

    List<String> getAllFeedbacks();
}
