package com.springboot.imagetst.service;

import java.util.List;
import com.springboot.imagetst.model.Product;
import com.springboot.imagetst.model.ProductDto;

public interface ProductService {
    List<Product> getAllProducts();
    void createProduct(ProductDto productDto);
    void deleteProduct(int id);
    Product getProductById(int id);
    void editProduct(int id, ProductDto productDto);
}