package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartItemException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.Product;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId);

    public String removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    public CartItem findCartItemById(Long cartItemId) throws  CartItemException;
}
