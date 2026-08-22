package com.shopsphere.ecommerce.dto.Cart;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {

    private Long cartId;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;

    public CartDTO() {
    }

    public CartDTO(
            Long cartId,
            List<CartItemDTO> items,
            BigDecimal totalPrice) {

        this.cartId = cartId;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}