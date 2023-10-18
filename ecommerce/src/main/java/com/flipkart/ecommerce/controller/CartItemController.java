package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.CartItemException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.AddItemRequest;
import com.flipkart.ecommerce.service.CartItemService;
import com.flipkart.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;


    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable Long cartItemId,
                                                   @RequestBody @Valid AddItemRequest item) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem cartItem = new CartItem();
        cartItem.setSize(item.getSize());
        cartItem.setQuantity(item.getQuantity());
        CartItem updatedItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.ACCEPTED);
    }


}
