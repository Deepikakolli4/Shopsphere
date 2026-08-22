package com.shopsphere.ecommerce.service.Cart;

import com.shopsphere.ecommerce.dto.Cart.CartDTO;
import com.shopsphere.ecommerce.entity.Cart.Cart;
import com.shopsphere.ecommerce.entity.Cart.CartItem;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.exception.CartValidationException;
import com.shopsphere.ecommerce.mapper.CartMapper;
import com.shopsphere.ecommerce.repository.Cart.CartItemRepository;
import com.shopsphere.ecommerce.repository.Cart.CartRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartMapper cartMapper) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }


    // ---------------------------------------------------------
    // ADD PRODUCT TO CART
    // ---------------------------------------------------------

    public CartDTO addToCart(Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new CartValidationException(
                    "Quantity must be greater than zero"
            );
        }

        User user = getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        if (!product.getAvailable()) {
            throw new CartValidationException(
                    "Product is not available"
            );
        }

        if (quantity > product.getStockQuantity()) {
            throw new CartValidationException(
                    "Requested quantity exceeds available stock"
            );
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

        } else {

            int newQuantity =
                    cartItem.getQuantity() + quantity;

            if (newQuantity > product.getStockQuantity()) {
                throw new CartValidationException(
                        "Requested quantity exceeds available stock"
                );
            }

            cartItem.setQuantity(newQuantity);
        }

        cartItemRepository.save(cartItem);

        return cartMapper.convertToDTO(cart);
    }


    // ---------------------------------------------------------
    // GET CURRENT USER'S CART
    // ---------------------------------------------------------

    public CartDTO getCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        return cartMapper.convertToDTO(cart);
    }


    // ---------------------------------------------------------
    // UPDATE CART ITEM QUANTITY
    // ---------------------------------------------------------

    public CartDTO updateQuantity(
            Long productId,
            Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new CartValidationException(
                    "Quantity must be greater than zero"
            );
        }

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product is not in cart"
                                )
                        );

        if (quantity > product.getStockQuantity()) {
            throw new CartValidationException(
                    "Requested quantity exceeds available stock"
            );
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return cartMapper.convertToDTO(cart);
    }


    // ---------------------------------------------------------
    // REMOVE PRODUCT FROM CART
    // ---------------------------------------------------------

    public CartDTO removeFromCart(Long productId) {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product is not in cart"
                                )
                        );

        cartItemRepository.delete(cartItem);

        return cartMapper.convertToDTO(cart);
    }


    // ---------------------------------------------------------
    // CLEAR CART
    // ---------------------------------------------------------

    public void clearCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        cartItemRepository.deleteAll(cart.getItems());
    }


    // ---------------------------------------------------------
    // GET CURRENT USER
    // ---------------------------------------------------------

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof User)) {

            throw new RuntimeException(
                    "User not authenticated"
            );
        }

        return (User) authentication.getPrincipal();
    }
}