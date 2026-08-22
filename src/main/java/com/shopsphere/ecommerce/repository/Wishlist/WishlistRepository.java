package com.shopsphere.ecommerce.repository.Wishlist;

import com.shopsphere.ecommerce.entity.Wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserIdAndProductId(
            Long userId,
            Long productId
    );

    List<Wishlist> findByUserId(Long userId);

    boolean existsByUserIdAndProductId(
            Long userId,
            Long productId
    );

    void deleteByUserIdAndProductId(
            Long userId,
            Long productId
    );
}