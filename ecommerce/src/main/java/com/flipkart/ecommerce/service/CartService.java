package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.request.AddItemRequest;

public interface CartService {
    public void createCart(User user);

    public String addCartItem(Long userId, AddItemRequest request) throws ProductException;

    public Cart findUserCart(Long userId) throws CartException;

}
