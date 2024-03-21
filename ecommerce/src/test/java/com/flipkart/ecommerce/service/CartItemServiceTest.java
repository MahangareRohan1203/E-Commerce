package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartItemException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Cart;
import com.flipkart.ecommerce.model.CartItem;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.CartItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    UserServiceImplementation userService;

    @InjectMocks
    CartItemServiceImplementation cartItemService;

    @Test
    void createCartItemSuccess() {
        // Given
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);
        cartItem.setProduct(product);

        CartItem expected = new CartItem();
        expected.setPrice(100);
        expected.setDiscountedPrice(80);
        expected.setProduct(product);
        expected.setQuantity(1);
        // When
        when(cartItemRepository.save(cartItem)).thenReturn(expected);
        CartItem response = cartItemService.createCartItem(cartItem);
        assertNotNull(response);
        assertEquals(expected.getQuantity(), response.getQuantity());
        assertEquals(expected.getProduct(), response.getProduct());
        assertEquals(expected.getPrice(), response.getPrice());
        assertEquals(expected.getDiscountedPrice(), response.getDiscountedPrice());


    }


    @Test
    void updateCartItemSuccess() throws CartItemException, UserException {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        Product product = new Product();
        CartItem existing = new CartItem();
        existing.setId(1L);
        existing.setQuantity(1);
        existing.setUserId(user.getId());
        existing.setProduct(product);

        CartItem cartItem1 = new CartItem();
        cartItem1.setQuantity(4);
        cartItem1.setId(1L);
        cartItem1.setUserId(user.getId());

        CartItem expected = new CartItem();
        expected.setQuantity(cartItem1.getQuantity());
        expected.setUserId(cartItem1.getUserId());

        // when
        when(cartItemRepository.findById(existing.getUserId())).thenReturn(Optional.of(existing));
        when(userService.findUserById(user.getId())).thenReturn(user);


        CartItem response = cartItemService.updateCartItem(1L, 1L, cartItem1);
        assertNotNull(response);
        assertEquals(expected.getQuantity(), response.getQuantity());
        assertEquals(expected.getUserId(), response.getUserId());
    }

    @Test
    void isCartItemExistsSuccess() {
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);


        CartItem expected = new CartItem();
        expected.setCart(cart);
        expected.setProduct(product);
        expected.setSize("S");

        when(cartItemRepository.isCartItemExists(cart, product, "S", 1L)).thenReturn(expected);
        CartItem response = cartItemService.isCartItemExists(cart, product, "S", 1L);
        assertNotNull(response);
        assertEquals(expected.getCart(), response.getCart());
        assertEquals(expected.getSize(), response.getSize());
        assertEquals(expected.getProduct(), response.getProduct());
        verify(cartItemRepository, times(1)).isCartItemExists(cart, product, "S", 1L);

    }

    @Test
    void isCartItemExistsFailure() {
        Cart cart = new Cart();
        Product product = new Product();

        when(cartItemRepository.isCartItemExists(cart, product, "S", 2L)).thenReturn(null);

        CartItem cartItem = cartItemService.isCartItemExists(cart, product, "S", 2L);
        assertNull(cartItem);

    }

    @Test
    void removeCartItemSuccess() throws CartItemException, UserException {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(1L);

        User user = new User();
        user.setId(1L);

        String expected = "Cart Item Removed ";
        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(user.getId())).thenReturn(user);
        String response = cartItemService.removeCartItem(1L, 1L);
        assertEquals(expected, response);
    }

    @Test
    void removeCartItemFailure() throws UserException {
        CartItem cartItem = new CartItem();
        User user = new User();
        user.setId(2L);
        cartItem.setUserId(2L);

        User different = new User();
        different.setId(1L);

        when(cartItemRepository.findById(2L)).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(user.getId())).thenReturn(user);
        when(userService.findUserById(different.getId())).thenReturn(different);
        Throwable thrown = catchThrowable(()->{
            cartItemService.removeCartItem(1L, 2L);
        });

        assertThat(thrown).hasMessage("You cannot remove another users Items ");
    }

    @Test
    void findCartItemByIdSuccess() throws CartItemException {
        CartItem existing = new CartItem();
        existing.setId(1L);
        existing.setQuantity(1);
        existing.setPrice(100);
        existing.setDiscountedPrice(80);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(existing));

        CartItem response = cartItemService.findCartItemById(existing.getId());
        assertNotNull(response);
        assertEquals(existing.getQuantity(), response.getQuantity());
        assertEquals(existing.getPrice(), response.getPrice());
        assertEquals(existing.getDiscountedPrice(), response.getDiscountedPrice());
    }

    @Test
    void findCartItemByIdFailure() {
        Long id = 2L;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> {
            CartItem response = cartItemService.findCartItemById(id);
        });
        assertThat(thrown).isInstanceOf(CartItemException.class).hasMessage("CartItem not found with Id: " + id);
    }
}