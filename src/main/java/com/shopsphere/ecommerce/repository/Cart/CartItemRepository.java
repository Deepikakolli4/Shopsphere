package com.shopsphere.ecommerce.repository.Cart;

import com.shopsphere.ecommerce.entity.Cart.Cart;
import com.shopsphere.ecommerce.entity.Cart.CartItem;
import com.shopsphere.ecommerce.entity.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(
            Cart cart,
            Product product
    );
}