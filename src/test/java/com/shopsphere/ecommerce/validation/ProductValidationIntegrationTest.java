package com.shopsphere.ecommerce.validation;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.User.Role;
import com.shopsphere.ecommerce.entity.User.User;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
class ProductValidationIntegrationTest {

    // ---------------------------------------------------------
    // TEST DATABASE
    // ---------------------------------------------------------

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4");


    // ---------------------------------------------------------
    // DEPENDENCIES
    // ---------------------------------------------------------

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    private String adminToken;

    private Long categoryId;


    // ---------------------------------------------------------
    // TEST SETUP
    // ---------------------------------------------------------

    @BeforeEach
    void setUp() {

        User admin = new User();

        admin.setName("Test Admin");
        admin.setEmail(
                "admin-" + System.nanoTime() + "@test.com"
        );
        admin.setPassword(
                passwordEncoder.encode("password123")
        );
        admin.setRole(Role.ADMIN);

        admin = userRepository.save(admin);

        adminToken = jwtService.generateToken(admin);


        Category category = new Category();

        category.setName(
                "Electronics-" + System.nanoTime()
        );

        category = categoryRepository.save(category);

        categoryId = category.getId();
    }


    // ---------------------------------------------------------
    // MISSING PRODUCT NAME
    // ---------------------------------------------------------

    @Test
    void createProduct_WithoutName_ShouldReturnBadRequest()
            throws Exception {

        String request = """
                {
                    "description": "Test product",
                    "price": 1000,
                    "brand": "Samsung",
                    "categoryId": %d,
                    "stockQuantity": 10,
                    "available": true
                }
                """.formatted(categoryId);

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Product name is required"));
    }


    // ---------------------------------------------------------
    // MISSING PRICE
    // ---------------------------------------------------------

    @Test
    void createProduct_WithoutPrice_ShouldReturnBadRequest()
            throws Exception {

        String request = """
                {
                    "name": "Test Product",
                    "description": "Test product",
                    "brand": "Samsung",
                    "categoryId": %d,
                    "stockQuantity": 10,
                    "available": true
                }
                """.formatted(categoryId);

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.price")
                        .value("Price is required"));
    }


    // ---------------------------------------------------------
    // NEGATIVE PRICE
    // ---------------------------------------------------------

    @Test
    void createProduct_WithNegativePrice_ShouldReturnBadRequest()
            throws Exception {

        String request = """
                {
                    "name": "Test Product",
                    "description": "Test product",
                    "price": -100,
                    "brand": "Samsung",
                    "categoryId": %d,
                    "stockQuantity": 10,
                    "available": true
                }
                """.formatted(categoryId);

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.price")
                        .value("Price cannot be negative"));
    }


    // ---------------------------------------------------------
    // MISSING CATEGORY ID
    // ---------------------------------------------------------

    @Test
    void createProduct_WithoutCategoryId_ShouldReturnBadRequest()
            throws Exception {

        String request = """
                {
                    "name": "Test Product",
                    "description": "Test product",
                    "price": 1000,
                    "brand": "Samsung",
                    "stockQuantity": 10,
                    "available": true
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.categoryId")
                        .value("Category ID is required"));
    }


    // ---------------------------------------------------------
    // PRODUCT NOT FOUND
    // ---------------------------------------------------------

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound()
            throws Exception {

        mockMvc.perform(
                        get("/products/999999")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }


    // ---------------------------------------------------------
    // INVALID CATEGORY
    // ---------------------------------------------------------

    @Test
    void createProduct_WithInvalidCategoryId_ShouldReturnNotFound()
            throws Exception {

        String request = """
                {
                    "name": "Test Product",
                    "description": "Test product",
                    "price": 1000,
                    "brand": "Samsung",
                    "categoryId": 999999999,
                    "stockQuantity": 10,
                    "available": true
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer " + adminToken
                                )
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}