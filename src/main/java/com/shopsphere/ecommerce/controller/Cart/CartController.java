package com.shopsphere.ecommerce.controller.Cart;

import com.shopsphere.ecommerce.dto.Cart.CartDTO;
import com.shopsphere.ecommerce.service.Cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add product to cart
    @PostMapping("/{productId}")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.addToCart(productId, quantity)
        );
    }

    // Get current user's cart
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {

        return ResponseEntity.ok(
                cartService.getCart()
        );
    }

    // Update product quantity
    @PutMapping("/{productId}")
    public ResponseEntity<CartDTO> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        productId,
                        quantity
                )
        );
    }

    // Remove one product from cart
    @DeleteMapping("/{productId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                cartService.removeFromCart(productId)
        );
    }

    // Clear entire cart
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();

        return ResponseEntity.noContent().build();
    }
}