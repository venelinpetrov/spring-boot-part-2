package com.codewithmosh.store.services;

import com.codewithmosh.store.config.AuthService;
import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentuser();
        var orders = orderRepository.getOrdersByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrderById(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentuser();

        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You are not authorized to perform this action");
        }

        return orderMapper.toDto(order);
    }
}
