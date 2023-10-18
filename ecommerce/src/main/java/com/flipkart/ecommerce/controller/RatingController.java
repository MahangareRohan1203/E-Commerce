package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.Exceptions.RatingException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Rating;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.RatingRequest;
import com.flipkart.ecommerce.service.RatingService;
import com.flipkart.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("")
    public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest rating,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ProductException, RatingException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating newRating = ratingService.createRating(rating, user);
        return new ResponseEntity<>(newRating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Rating>> getAllRatingsForProductHandler(@PathVariable Long id) {
        List<Rating> list = ratingService.getProductsRating(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
