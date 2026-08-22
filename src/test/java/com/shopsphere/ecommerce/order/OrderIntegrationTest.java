package com.shopsphere.ecommerce.order;

import com.shopsphere.ecommerce.entity.Cart.Cart;
import com.shopsphere.ecommerce.entity.Cart.CartItem;
import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.Role;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.repository.Cart.CartItemRepository;
import com.shopsphere.ecommerce.repository.Cart.CartRepository;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.User.UserRepository;
import com.shopsphere.ecommerce.service.Auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private User customer;
    private Product product;
    private Cart cart;

    private String token;


    @BeforeEach
    void setUp() {

        // ---------------------------------------------
        // CREATE CUSTOMER
        // ---------------------------------------------

        customer = new User();

        customer.setName("Order Test Customer");

        customer.setEmail(
                "order-" + System.nanoTime() + "@test.com"
        );

        customer.setPassword(
                passwordEncoder.encode("password123")
        );

        customer.setRole(Role.CUSTOMER);

        customer = userRepository.save(customer);


        Category category = new Category();

        category.setName(
                "Electronics-" + System.nanoTime()
        );

        category = categoryRepository.save(category);

        product = new Product();

        product.setName("Test Laptop");

        product.setBrand("Test Brand");

        product.setCategory(category);

        product.setPrice(
                new BigDecimal("50000.00")
        );

        product.setStockQuantity(10);

        product.setAvailable(true);

        product = productRepository.save(product);

        cart = new Cart();

        cart.setUser(customer);

        cart = cartRepository.save(cart);

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);

        cartItem.setProduct(product);

        cartItem.setQuantity(2);

        cartItemRepository.save(cartItem);

        token = jwtService.generateToken(customer);
    }

    @Test
    void placeOrder_ShouldReturnOk() throws Exception {

        mockMvc.perform(
                        post("/orders")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void getMyOrders_ShouldReturnOk() throws Exception {

        // First place an order

        mockMvc.perform(
                post("/orders")
                        .header(
                                "Authorization",
                                "Bearer " + token
                        )
        ).andExpect(status().isOk());


        // Then get orders

        mockMvc.perform(
                        get("/orders")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }
}