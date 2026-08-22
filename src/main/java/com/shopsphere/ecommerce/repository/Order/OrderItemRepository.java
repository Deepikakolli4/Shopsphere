package com.shopsphere.ecommerce.repository.Order;

import com.shopsphere.ecommerce.entity.Order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}