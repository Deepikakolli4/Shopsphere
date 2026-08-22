package com.shopsphere.ecommerce.mapper;

import com.shopsphere.ecommerce.dto.Order.OrderDTO;
import com.shopsphere.ecommerce.dto.Order.OrderItemDTO;
import com.shopsphere.ecommerce.entity.Order.Order;
import com.shopsphere.ecommerce.entity.Order.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderDTO convertToDTO(Order order) {

        OrderDTO dto = new OrderDTO();

        dto.setId(order.getId());

        dto.setStatus(
                order.getStatus()
        );

        // Order entity uses Double totalAmount
        // Convert it to BigDecimal for the DTO
        dto.setTotalAmount(
                BigDecimal.valueOf(
                        order.getTotalAmount()
                )
        );

        dto.setCreatedAt(
                order.getCreatedAt()
        );

        List<OrderItemDTO> itemDTOs =
                order.getItems()
                        .stream()
                        .map(this::convertItemToDTO)
                        .toList();

        dto.setItems(itemDTOs);

        return dto;
    }


    private OrderItemDTO convertItemToDTO(
            OrderItem orderItem) {

        OrderItemDTO dto = new OrderItemDTO();

        dto.setProductId(
                orderItem.getProduct().getId()
        );

        dto.setProductName(
                orderItem.getProduct().getName()
        );

        dto.setQuantity(
                orderItem.getQuantity()
        );

        dto.setPrice(
                orderItem.getPrice()
        );

        return dto;
    }
}