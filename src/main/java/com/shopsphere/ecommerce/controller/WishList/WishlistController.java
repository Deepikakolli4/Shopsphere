package com.shopsphere.ecommerce.controller.WishList;

import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.service.Wishlist.WishlistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService) {

        this.wishlistService = wishlistService;
    }


    @PostMapping("/{productId}")
    public ResponseEntity<Void> addToWishlist(
            @PathVariable Long productId) {

        wishlistService.addToWishlist(productId);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long productId) {

        wishlistService.removeFromWishlist(productId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<Product>> getMyWishlist() {

        return ResponseEntity.ok(
                wishlistService.getMyWishlist()
        );
    }
}