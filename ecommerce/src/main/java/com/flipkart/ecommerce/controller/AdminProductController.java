package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.request.CreateProductRequest;
import com.flipkart.ecommerce.response.ApiResponse;
import com.flipkart.ecommerce.service.ProductServiceImplementation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    @Autowired
    private ProductServiceImplementation productServiceImplementation;

    @PostMapping("")
    public ResponseEntity<Product> addNewProductHandler(@RequestBody @Valid CreateProductRequest request) {
        Product product = productServiceImplementation.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {
        var message = productServiceImplementation.deleteProduct(productId);
        ApiResponse response = new ApiResponse(message, true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProductsHandler() {
        List<Product> list = productServiceImplementation.findAllProducts();
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProductHandler(@PathVariable Long productId, @RequestBody Product newProduct) throws ProductException {
        Product existingProduct = productServiceImplementation.findProductById(productId);
        Product product = productServiceImplementation.updateProduct(productId, newProduct);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        Product product = productServiceImplementation.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


}
