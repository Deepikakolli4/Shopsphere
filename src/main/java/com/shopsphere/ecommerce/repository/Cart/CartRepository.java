package com.shopsphere.ecommerce.repository.Cart;

import com.shopsphere.ecommerce.entity.Cart.Cart;
import com.shopsphere.ecommerce.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}