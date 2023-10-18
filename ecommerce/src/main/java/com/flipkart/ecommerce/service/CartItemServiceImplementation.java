package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartItemException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.CartItemRepository;
import com.flipkart.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImplementation implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getDiscountedPrice() * cartItem.getQuantity());
        cartItem = cartItemRepository.save(cartItem);
        return cartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        //TODO : BELOW IS cartItem.getUserId()
        User user = userService.findUserById(item.getUserId());
        if (user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setSize(cartItem.getSize());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
            cartItemRepository.save(item);
        } else throw new CartItemException("User Not matches");
        return item;
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = cartItemRepository.isCartItemExists(cart, product, size, userId);

        return cartItem;
    }

    @Override
    public String removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemException("Invalid CartItem id"));
        User user = userService.findUserById(cartItem.getUserId());
        User requestUser = userService.findUserById(userId);
        if (user.getId().equals(requestUser.getId())) {
            cartItemRepository.delete(cartItem);
        } else throw new UserException("You cannot remove another users Items ");
        return "Cart Item Removed ";
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemException("CartItem not found with Id: " + cartItemId));
        return cartItem;
    }
}
