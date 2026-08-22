package com.shopsphere.ecommerce.mapper;

import com.shopsphere.ecommerce.dto.Cart.CartDTO;
import com.shopsphere.ecommerce.dto.Cart.CartItemDTO;
import com.shopsphere.ecommerce.entity.Cart.Cart;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {
    public CartDTO convertToDTO(Cart cart) {

        List<CartItemDTO> items = cart.getItems()
                .stream()
                .map(item -> {

                    BigDecimal subtotal =
                            item.getProduct()
                                    .getPrice()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    );

                    return new CartItemDTO(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getBrand(),
                            item.getProduct().getPrice(),
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return new CartDTO(
                cart.getId(),
                items,
                totalPrice
        );
    }
}
