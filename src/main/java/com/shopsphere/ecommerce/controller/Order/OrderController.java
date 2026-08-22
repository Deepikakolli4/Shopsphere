package com.shopsphere.ecommerce.controller.Order;

import com.shopsphere.ecommerce.dto.Order.OrderDTO;
import com.shopsphere.ecommerce.service.Order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder() {

        return ResponseEntity.ok(
                orderService.placeOrder()
        );
    }



    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders() {

        return ResponseEntity.ok(
                orderService.getMyOrders()
        );
    }
}