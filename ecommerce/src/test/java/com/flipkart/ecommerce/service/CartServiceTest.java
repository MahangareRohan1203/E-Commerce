package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.CartRepository;
import com.flipkart.ecommerce.request.AddItemRequest;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImplementation cartService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void createCart() {
        User user = new User();
        user.setId(1L);

        Cart expected = new Cart();
        expected.setUser(user);

        when(cartRepository.save(any(Cart.class))).thenReturn(expected);

        cartService.createCart(user);
    }

    @Test
    void addCartItemSuccess() throws ProductException {
        User user = new User();
        user.setId(1L);

        AddItemRequest cartItem = new AddItemRequest();
        cartItem.setPrice(120);
        cartItem.setSize("S");
        cartItem.setQuantity(1);
        cartItem.setProductId(1L);

        Cart cart = new Cart();
        cart.setUser(user);

        Product product = new Product();
        product.setId(1L);

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(productService.findProductById(product.getId())).thenReturn(product);
        when(cartItemService.isCartItemExists(cart, product, cartItem.getSize(), user.getId())).thenReturn(null);

        String response = cartService.addCartItem(1L, cartItem);

        assertNotNull(response);
        assertEquals("Item Added to Cart", response);

    }

    @Test
    void findUserCart() throws CartException {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new HashSet<>());
        User user = new User();
        user.setId(1L);
        cart.setUser(user);


        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart response =  cartService.findUserCart(1L);
        assertThat(response).isNotNull();
        assertEquals(cart.getUser(), response.getUser());
        verify(cartRepository, times(1)).findByUserId(any(Long.class));

    }
}