package com.shopsphere.ecommerce.service.Order;

import com.shopsphere.ecommerce.dto.Order.OrderDTO;
import com.shopsphere.ecommerce.entity.Cart.Cart;
import com.shopsphere.ecommerce.entity.Cart.CartItem;
import com.shopsphere.ecommerce.entity.Order.Order;
import com.shopsphere.ecommerce.entity.Order.OrderItem;
import com.shopsphere.ecommerce.entity.Order.OrderStatus;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.mapper.OrderMapper;
import com.shopsphere.ecommerce.repository.Cart.CartRepository;
import com.shopsphere.ecommerce.repository.Order.OrderRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            OrderMapper orderMapper) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderDTO placeOrder() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            // Check availability
            if (!product.getAvailable()) {
                throw new RuntimeException(
                        product.getName() + " is not available"
                );
            }

            // Check stock
            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for " + product.getName()
                );
            }

            // Create order item
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.getItems().add(orderItem);

            // Calculate item total
            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            );

            // Add to order total
            totalAmount = totalAmount.add(itemTotal);

            // Reduce product stock
            product.setStockQuantity(
                    product.getStockQuantity()
                            - cartItem.getQuantity()
            );
        }

        // IMPORTANT:
        // Order.totalAmount is Double
        order.setTotalAmount(
                totalAmount.doubleValue()
        );

        Order savedOrder =
                orderRepository.save(order);

        // Clear cart after successful order
        cart.getItems().clear();

        cartRepository.save(cart);

        return orderMapper.convertToDTO(
                savedOrder
        );
    }

    public List<OrderDTO> getMyOrders() {

        User user = getCurrentUser();

        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::convertToDTO)
                .toList();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal()
                        instanceof User)) {

            throw new RuntimeException(
                    "User not authenticated"
            );
        }

        return (User) authentication.getPrincipal();
    }
}