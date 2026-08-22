package com.shopsphere.ecommerce.entity.Product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shopsphere.ecommerce.entity.Category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
//Indexing
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_brand", columnList = "brand"),
                @Index(name = "idx_product_price", columnList = "price"),
                @Index(name = "idx_product_available", columnList = "available"),
                @Index(name = "idx_product_category", columnList = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be empty")
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    @Column(nullable = false)
    private BigDecimal price;

    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotNull(message = "Category is required")
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private Boolean available;

    @Column(updatable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

}