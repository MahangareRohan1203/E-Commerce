package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.Review;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.ReviewRepository;
import com.flipkart.ecommerce.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Review createReview(ReviewRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        if (product == null) throw new ProductException("Product not found");
        Review review = new Review();
        review.setReview(request.getReview());
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);
        return review;
    }

    @Override
    public List<Review> getAllReviewsByProductId(Long productId) {
        List<Review> list = reviewRepository.getAllReviewsByProductId(productId);
        return list;
    }
}
