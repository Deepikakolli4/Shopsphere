package com.shopsphere.ecommerce.security;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.entity.User.Role;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.User.UserRepository;
import com.shopsphere.ecommerce.service.Auth.JwtService;

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
class SecurityIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;


    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------

    @Test
    void login_WithValidCredentials_ShouldReturnToken()
            throws Exception {

        User user = new User();

        user.setName("Test Customer");
        user.setEmail("customer@test.com");
        user.setPassword(
                passwordEncoder.encode("password123")
        );
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        String request = """
                {
                    "email": "customer@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }


    @Test
    void login_WithWrongPassword_ShouldReturnUnauthorized()
            throws Exception {

        User user = new User();

        user.setName("Test Customer");
        user.setEmail("wrongpassword@test.com");
        user.setPassword(
                passwordEncoder.encode("password123")
        );
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        String request = """
                {
                    "email": "wrongpassword@test.com",
                    "password": "wrongPassword"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isUnauthorized());
    }


    // ---------------------------------------------------------
    // PROTECTED API
    // ---------------------------------------------------------

    @Test
    void getProducts_WithoutJwt_ShouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                )
                .andExpect(status().isUnauthorized());
    }


    // ---------------------------------------------------------
    // CUSTOMER → ADMIN API
    // ---------------------------------------------------------

    @Test
    void customer_CannotDeleteProduct_ShouldReturnForbidden()
            throws Exception {

        User customer = new User();

        customer.setName("Customer");
        customer.setEmail("securitycustomer@test.com");
        customer.setPassword(
                passwordEncoder.encode("password123")
        );
        customer.setRole(Role.CUSTOMER);

        customer = userRepository.save(customer);

        String token =
                jwtService.generateToken(customer);

        mockMvc.perform(
                        delete("/products/1")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isForbidden());
    }


    // ---------------------------------------------------------
    // ADMIN → ADMIN API
    // ---------------------------------------------------------

    @Test
    void admin_CanDeleteProduct_ShouldReturnNoContent()
            throws Exception {

        Category category = new Category();
        category.setName("Electronics");

        category = categoryRepository.save(category);


        Product product = new Product();

        product.setName("Test Product");
        product.setDescription(
                "Product for security integration test"
        );
        product.setPrice(new BigDecimal("1000.00"));
        product.setBrand("TestBrand");
        product.setStockQuantity(10);
        product.setAvailable(true);
        product.setCategory(category);

        product = productRepository.save(product);


        User admin = new User();

        admin.setName("Admin");
        admin.setEmail("securityadmin@test.com");
        admin.setPassword(
                passwordEncoder.encode("password123")
        );
        admin.setRole(Role.ADMIN);

        admin = userRepository.save(admin);


        String token =
                jwtService.generateToken(admin);


        mockMvc.perform(
                        delete("/products/" + product.getId())
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNoContent());
    }


    // ---------------------------------------------------------
    // INVALID JWT
    // ---------------------------------------------------------

    @Test
    void getProducts_WithInvalidJwt_ShouldReturnUnauthorized()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                                .header(
                                        "Authorization",
                                        "Bearer invalid.jwt.token"
                                )
                )
                .andExpect(status().isUnauthorized());
    }
}