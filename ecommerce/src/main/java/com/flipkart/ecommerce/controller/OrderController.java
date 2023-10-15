package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.CartException;
import com.flipkart.ecommerce.Exceptions.OrderException;
import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.Address;
import com.flipkart.ecommerce.model.Order;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.service.OrderServiceImplementation;
import com.flipkart.ecommerce.service.UserService;
import com.flipkart.ecommerce.service.UserServiceImplementation;
import org.hibernate.event.spi.ResolveNaturalIdEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private UserServiceImplementation userService;

    @Autowired
    private OrderServiceImplementation orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws UserException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddress);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orderList = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orderList, HttpStatus.ACCEPTED);
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<Order> findOrderByOrderIdHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.findOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }


}
