package com.springboot.imagetst.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.imagetst.model.Product;
import com.springboot.imagetst.model.ProductDto;
import com.springboot.imagetst.repository.ProductRepository;
import com.springboot.imagetst.util.ImageUtil;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public void createProduct(ProductDto productDto) {
    	
        String imageBase64 = ImageUtil.convertImageToBase64(productDto.getImageFile());
        Date createdAt = new Date();

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageData(imageBase64); // Save base64 string to the entity
        repo.save(product);
    }
    
    @Override
    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void editProduct(int id, ProductDto productDto) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
 
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        if (!productDto.getImageFile().isEmpty()) {
            String imageBase64 = ImageUtil.convertImageToBase64(productDto.getImageFile());
            product.setImageData(imageBase64); // Update base64 string
        }
        repo.save(product);
    }
    
    @Override
    public void deleteProduct(int id) {
        repo.deleteById(id);
    }
}
