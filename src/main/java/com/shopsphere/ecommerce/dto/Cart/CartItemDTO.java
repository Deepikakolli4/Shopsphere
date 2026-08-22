package com.shopsphere.ecommerce.dto.Cart;

import java.math.BigDecimal;

public class CartItemDTO {

    private Long productId;
    private String productName;
    private String brand;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public CartItemDTO() {
    }

    public CartItemDTO(
            Long productId,
            String productName,
            String brand,
            BigDecimal price,
            Integer quantity,
            BigDecimal subtotal) {

        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}