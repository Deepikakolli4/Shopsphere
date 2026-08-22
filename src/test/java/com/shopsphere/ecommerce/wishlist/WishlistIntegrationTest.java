package com.shopsphere.ecommerce.wishlist;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
class WishlistIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CategoryRepository categoryRepository;


    private User customer;
    private Category category;
    private Product product;



    @BeforeEach
    void setUp() {

        // Create customer
        customer = new User();

        customer.setName("Wishlist Customer");
        customer.setEmail(
                "wishlist-" + System.nanoTime() + "@test.com"
        );
        customer.setPassword(
                passwordEncoder.encode("password123")
        );
        customer.setRole(Role.CUSTOMER);

        customer = userRepository.save(customer);


        // Create category
        category = new Category();

        category.setName("Electronics");

        category = categoryRepository.save(category);


        // Create product
        product = new Product();

        product.setName("Test Product");
        product.setBrand("Test Brand");
        product.setDescription("Test product for wishlist");
        product.setPrice(new BigDecimal("999.99"));
        product.setStockQuantity(10);
        product.setAvailable(true);
        product.setCategory(category);

        product = productRepository.save(product);
    }


    // ---------------------------------------------------------
    // ADD TO WISHLIST
    // ---------------------------------------------------------

    @Test
    void addProductToWishlist_ShouldReturnOk()
            throws Exception {

        Long productId = 1L;

        String token = jwtService.generateToken(customer);

        mockMvc.perform(
                        post("/wishlist/{productId}", productId)
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // UNAUTHORIZED USER
    // ---------------------------------------------------------

    @Test
    void addToWishlist_WithoutJwt_ShouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        post("/wishlist/1")
                )
                .andExpect(status().isUnauthorized());
    }


    // ---------------------------------------------------------
    // REMOVE FROM WISHLIST
    // ---------------------------------------------------------

    @Test
    void removeProductFromWishlist_ShouldReturnNoContent()
            throws Exception {

        Long productId = 1L;

        String token = jwtService.generateToken(customer);

        mockMvc.perform(
                        delete("/wishlist/{productId}", productId)
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNoContent());
    }


    // ---------------------------------------------------------
    // GET WISHLIST
    // ---------------------------------------------------------

    @Test
    void getMyWishlist_ShouldReturnOk()
            throws Exception {

        String token = jwtService.generateToken(customer);

        mockMvc.perform(
                        get("/wishlist")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }


    // ---------------------------------------------------------
    // INVALID JWT
    // ---------------------------------------------------------

    @Test
    void getWishlist_WithInvalidJwt_ShouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get("/wishlist")
                                .header(
                                        "Authorization",
                                        "Bearer invalid.jwt.token"
                                )
                )
                .andExpect(status().isUnauthorized());
    }
}