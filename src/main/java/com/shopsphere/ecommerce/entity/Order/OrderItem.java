package com.shopsphere.ecommerce.entity.Order;

import com.shopsphere.ecommerce.entity.Product.Product;
import jakarta.persistence.*;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    @Setter
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;



    @Setter
    @Column(nullable = false)
    private Integer quantity;

    @Setter
    @Column(nullable = false)
    private BigDecimal price;


    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

}