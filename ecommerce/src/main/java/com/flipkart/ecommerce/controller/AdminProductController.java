package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.request.CreateProductRequest;
import com.flipkart.ecommerce.service.ProductServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminProductController {

    @Autowired
    private ProductServiceImplementation productServiceImplementation;

    @PostMapping("/products")
    public ResponseEntity<Product> addNewProductHandler(@RequestBody @Valid CreateProductRequest request) {
        Product product = productServiceImplementation.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
}
