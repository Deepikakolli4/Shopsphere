package com.shopsphere.ecommerce.cart;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.Role;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.User.UserRepository;
import com.shopsphere.ecommerce.service.Auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
class CartIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    private User customer;
    private Category category;
    private Product product;

    private String token;


    // ---------------------------------------------------------
    // SETUP
    // ---------------------------------------------------------

    @BeforeEach
    void setUp() {

        // Create customer

        customer = new User();

        customer.setName("Cart Customer");

        customer.setEmail(
                "cart-" + System.nanoTime() + "@test.com"
        );

        customer.setPassword(
                passwordEncoder.encode("password123")
        );

        customer.setRole(Role.CUSTOMER);

        customer = userRepository.save(customer);


        // Create category

        category = new Category();

        category.setName(
                "Cart Test Category"
        );

        category = categoryRepository.save(category);


        // Create product

        product = new Product();

        product.setName("Cart Test Product");
        product.setBrand("Test Brand");
        product.setDescription("Product for cart testing");
        product.setPrice(
                new BigDecimal("999.99")
        );
        product.setStockQuantity(10);
        product.setAvailable(true);
        product.setCategory(category);

        product = productRepository.save(product);


        // Generate JWT

        token = jwtService.generateToken(customer);
    }


    // ---------------------------------------------------------
    // ADD TO CART
    // ---------------------------------------------------------

    @Test
    void addProductToCart_ShouldReturnOk()
            throws Exception {

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "2")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // GET CART
    // ---------------------------------------------------------

    @Test
    void getCart_ShouldReturnOk()
            throws Exception {

        mockMvc.perform(
                        get("/cart")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // UPDATE QUANTITY
    // ---------------------------------------------------------

    @Test
    void updateCartQuantity_ShouldReturnOk()
            throws Exception {

        // First add product

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "2")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());


        // Update quantity

        mockMvc.perform(
                        put("/cart/" + product.getId())
                                .param("quantity", "5")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // REMOVE FROM CART
    // ---------------------------------------------------------

    @Test
    void removeProductFromCart_ShouldReturnOk()
            throws Exception {

        // Add product

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "2")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());


        // Remove product

        mockMvc.perform(
                        delete("/cart/" + product.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // CLEAR CART
    // ---------------------------------------------------------

    @Test
    void clearCart_ShouldReturnNoContent()
            throws Exception {

        // Add product

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "2")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());


        // Clear cart

        mockMvc.perform(
                        delete("/cart")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNoContent());
    }


    // ---------------------------------------------------------
    // ADD PRODUCT WITHOUT JWT
    // ---------------------------------------------------------

    @Test
    void addProductToCart_WithoutJwt_ShouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "2")
                )
                .andExpect(status().isUnauthorized());
    }


    // ---------------------------------------------------------
    // INVALID QUANTITY
    // ---------------------------------------------------------

    @Test
    void addProductToCart_WithInvalidQuantity_ShouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "0")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isBadRequest());
    }


    // ---------------------------------------------------------
    // QUANTITY EXCEEDS STOCK
    // ---------------------------------------------------------

    @Test
    void addProductToCart_QuantityExceedsStock_ShouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                        post("/cart/" + product.getId())
                                .param("quantity", "100")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isBadRequest());
    }
}