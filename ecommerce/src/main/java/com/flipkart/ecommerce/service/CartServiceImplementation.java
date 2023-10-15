package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.CartRepository;
import com.flipkart.ecommerce.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        Set<CartItem> productsList = new HashSet<>();
        cart.setCartItems(productsList);
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(request.getProductId());
        CartItem isPresent = cartItemService.isCartItemExists(cart, product, request.getSize(), userId);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setDiscountedPrice(product.getDiscountedPrice());
            cartItem.setPrice(product.getDiscountedPrice() * cartItem.getQuantity());
            cartItem.setSize(request.getSize());


            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            System.out.println(createdCartItem);
            cart.getCartItems().add(createdCartItem);
            System.out.println(cart);
        } else return "Product Already Exists in card";
        return "Item Added to Cart";
    }

    @Override
    public Cart findUserCart(Long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalItem += cartItem.getQuantity();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItems(totalItem);
        cart.setDiscount(totalPrice - totalDiscountedPrice);
        cartRepository.save(cart);
        System.out.println("CART IS " + cart);
        return cart;
    }
}
