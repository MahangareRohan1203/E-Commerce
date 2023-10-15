package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.CartItemException;
import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.AddItemRequest;
import com.flipkart.ecommerce.service.CartItemService;
import com.flipkart.ecommerce.service.CartItemServiceImplementation;
import com.flipkart.ecommerce.service.CartServiceImplementation;
import com.flipkart.ecommerce.service.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartServiceImplementation cartService;

    @Autowired
    private CartItemServiceImplementation cartItemService;

    @Autowired
    private UserServiceImplementation userService;

    @GetMapping("/cart")
    public ResponseEntity<Cart> cartHandler(@RequestHeader("Authorization") String jwt) throws CartException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
    }

    @PostMapping("/cart")
    public ResponseEntity<String> addProductToCart(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest addItemRequest) throws UserException, ProductException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        String response = cartService.addCartItem(user.getId(), addItemRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> removeProductFromCartHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        String response = cartItemService.removeCartItem(user.getId(), id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
