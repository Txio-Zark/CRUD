package com.springboot.imagetst.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import com.springboot.imagetst.model.Product;
import com.springboot.imagetst.model.ProductDto;
import com.springboot.imagetst.service.ProductService;
import com.springboot.imagetst.util.ImageUtil;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
	
    @Autowired
    private ProductService productService;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/index";
    }
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}
	
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
    	
		if (productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto","imageFile", "The image file is required"));
		}
		
        if (result.hasErrors()) {
            return "products/CreateProduct";
        }
        
     // Check if there's an error reading the image
        if (result.getErrorCount() == 0) {
            String base64Image = ImageUtil.convertImageToBase64(productDto.getImageFile());
            if (base64Image == null) {
                result.addError(new FieldError("productDto", "imageFile", "Error reading the image file (Pls use the correct format) "));
                return "products/CreateProduct";
            } 
        }

        productService.createProduct(productDto);
        return "redirect:/products";
    }
     
    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return "redirect:/products";
            }
            model.addAttribute("product", product);
            
            // Create a new ProductDto object and set its properties from the retrieved product
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            // Add the productDto object to the model
            model.addAttribute("productDto", productDto);
            
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }
    
    @PostMapping("/edit")
    public String editProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        try {       	
        	Product product = productService.getProductById(id);
        	
        	model.addAttribute("product", product);
        	model.addAttribute("productDto", productDto);
        	
            if (result.hasErrors()) {
                return "products/EditProduct";
            }
        	
            // Check if there's an error reading the image
            if (!productDto.getImageFile().isEmpty()) {
                String base64Image = ImageUtil.convertImageToBase64(productDto.getImageFile());
                if (base64Image == null) {
                    result.addError(new FieldError("productDto", "imageFile", "Error reading the image file (Pls use the correct format) "));
                    return "products/EditProduct";
                } 
            }
        	
            productService.editProduct(id, productDto);
            // Redirect to the product list page after editing
            return "redirect:/products";
            
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            return "redirect:/products";
        }
    }
    
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        try {
            productService.deleteProduct(id);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            // Handle exception if needed
        }
        // Redirect to the product list page after deletion
        return "redirect:/products";
    }
    
    
}
