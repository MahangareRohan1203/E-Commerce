package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.Exceptions.RatingException;
import com.flipkart.ecommerce.model.Rating;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest request, User user) throws ProductException, RatingException;

    public List<Rating> getProductsRating(Long productId);
}
