package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.model.OrderItem;
import com.flipkart.ecommerce.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplementationTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    @Test
    void createOrderItemSuccess() {
        OrderItem item = new OrderItem();
        item.setId(1L);
        when(orderItemRepository.save(item)).thenReturn(item);
        OrderItem response = orderItemService.createOrderItem(item);
        assertNotNull(response);
        assertEquals(item, response);
    }
}