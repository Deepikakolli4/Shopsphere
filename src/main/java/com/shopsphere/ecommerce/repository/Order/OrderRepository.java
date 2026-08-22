package com.shopsphere.ecommerce.repository.Order;

import com.shopsphere.ecommerce.entity.Order.Order;
import com.shopsphere.ecommerce.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}