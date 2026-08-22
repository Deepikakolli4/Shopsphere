package com.shopsphere.ecommerce.service.Wishlist;

import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.entity.Wishlist.Wishlist;
import com.shopsphere.ecommerce.exception.ProductNotFoundException;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.User.UserRepository;
import com.shopsphere.ecommerce.repository.Wishlist.WishlistRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    // ---------------------------------------------------------
    // LIKE PRODUCT
    // ---------------------------------------------------------

    public void addToWishlist(Long productId) {

        User user = getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        if (wishlistRepository.existsByUserIdAndProductId(
                user.getId(),
                productId)) {

            return;
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
    }


    // ---------------------------------------------------------
    // REMOVE FROM WISHLIST
    // ---------------------------------------------------------

    public void removeFromWishlist(Long productId) {

        User user = getCurrentUser();

        wishlistRepository.deleteByUserIdAndProductId(
                user.getId(),
                productId
        );
    }


    // ---------------------------------------------------------
    // GET MY WISHLIST
    // ---------------------------------------------------------

    public List<Product> getMyWishlist() {

        User user = getCurrentUser();

        return wishlistRepository
                .findByUserId(user.getId())
                .stream()
                .map(Wishlist::getProduct)
                .toList();
    }


    // ---------------------------------------------------------
    // CURRENT USER
    // ---------------------------------------------------------

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof User)) {

            throw new RuntimeException("User not authenticated");
        }

        return (User) authentication.getPrincipal();
    }
}