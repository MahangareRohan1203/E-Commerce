package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.Rating;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.RatingRepository;
import com.flipkart.ecommerce.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class RatingServiceImplementation implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Rating createRating(RatingRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setRating(request.getRating());
        rating.setUser(user);
        rating.setCreatedAt(LocalDateTime.now());
        rating = ratingRepository.save(rating);
        return rating;
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        List<Rating> list = ratingRepository.getAllProductsRating(productId);
        return list;
    }
}
