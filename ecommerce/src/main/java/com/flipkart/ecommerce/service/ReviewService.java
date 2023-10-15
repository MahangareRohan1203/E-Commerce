package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Review;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest request, User user) throws ProductException;

    public List<Review> getAllReviewsByProductId(Long productId);
}
