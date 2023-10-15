package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.OrderException;
import com.flipkart.ecommerce.model.Address;
import com.flipkart.ecommerce.model.Order;
import com.flipkart.ecommerce.model.User;

import java.util.List;

public interface OrderService {
    public Order createOrder(User user, Address shippingAddress) throws CartException;

    public Order findOrderById(Long id) throws OrderException;

    public List<Order> userOrderHistory(Long userId);

    public Order placeOrder(Long orderId) throws OrderException;

    public Order confirmOrder(Long orderId) throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliverOrder(Long orderId) throws OrderException;

    public Order cancelOrder(Long orderId) throws OrderException;

    public List<Order> getAllOrders();

    public String deleteOrder(Long orderId) throws OrderException;

}
