package com.shopsphere.ecommerce.IntegrityTest;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("shopsphere_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add(
                "spring.datasource.driver-class-name",
                mysql::getDriverClassName
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category electronics;
    private Category clothing;

    private Product samsung;
    private Product iphone;
    private Product nikeShoes;
    private Product adidasShoes;


    // ---------------------------------------------------------
    // TEST DATA SETUP
    // ---------------------------------------------------------

    @BeforeEach
    void setUp() {

        productRepository.deleteAll();
        categoryRepository.deleteAll();

        electronics = new Category();
        electronics.setName("Electronics");
        electronics = categoryRepository.save(electronics);

        clothing = new Category();
        clothing.setName("Clothing");
        clothing = categoryRepository.save(clothing);


        samsung = new Product();
        samsung.setName("Samsung S25 Ultra");
        samsung.setDescription("Samsung flagship smartphone");
        samsung.setPrice(new BigDecimal("95000"));
        samsung.setBrand("Samsung");
        samsung.setStockQuantity(10);
        samsung.setAvailable(true);
        samsung.setCategory(electronics);
        samsung = productRepository.save(samsung);


        iphone = new Product();
        iphone.setName("Iphone 17");
        iphone.setDescription("Apple smartphone");
        iphone.setPrice(new BigDecimal("95000"));
        iphone.setBrand("Apple");
        iphone.setStockQuantity(5);
        iphone.setAvailable(true);
        iphone.setCategory(electronics);
        iphone = productRepository.save(iphone);


        nikeShoes = new Product();
        nikeShoes.setName("Nike Air Max");
        nikeShoes.setDescription("Running shoes");
        nikeShoes.setPrice(new BigDecimal("8000"));
        nikeShoes.setBrand("Nike");
        nikeShoes.setStockQuantity(20);
        nikeShoes.setAvailable(true);
        nikeShoes.setCategory(clothing);
        nikeShoes = productRepository.save(nikeShoes);


        adidasShoes = new Product();
        adidasShoes.setName("Adidas Runner");
        adidasShoes.setDescription("Sports shoes");
        adidasShoes.setPrice(new BigDecimal("6000"));
        adidasShoes.setBrand("Adidas");
        adidasShoes.setStockQuantity(15);
        adidasShoes.setAvailable(false);
        adidasShoes.setCategory(clothing);
        adidasShoes = productRepository.save(adidasShoes);
    }


    // ---------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------

    @Test
    void createProduct_ShouldSaveProductAndReturnResponse() throws Exception {

        String requestJson = """
                {
                    "name": "Nothing Phone",
                    "description": "Nothing smartphone",
                    "price": 90000,
                    "brand": "Nothing",
                    "categoryId": %d,
                    "stockQuantity": 10,
                    "available": true
                }
                """.formatted(electronics.getId());

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Nothing Phone"))
                .andExpect(jsonPath("$.price")
                        .value(90000.0))
                .andExpect(jsonPath("$.categoryName")
                        .value("Electronics"));
    }


    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {

        mockMvc.perform(
                        get("/products/{id}", samsung.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(samsung.getId().intValue()))
                .andExpect(jsonPath("$.name")
                        .value("Samsung S25 Ultra"))
                .andExpect(jsonPath("$.price")
                        .value(95000.0))
                .andExpect(jsonPath("$.categoryName")
                        .value("Electronics"));
    }


    // ---------------------------------------------------------
    // GET ALL PRODUCTS + PAGINATION
    // ---------------------------------------------------------

    @Test
    void getAllProducts_ShouldReturnPaginatedProducts() throws Exception {

        mockMvc.perform(
                        get("/products")
                                .param("page", "0")
                                .param("size", "20")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content")
                        .isArray())
                .andExpect(jsonPath("$.content",
                        hasSize(4)))
                .andExpect(jsonPath("$.totalElements")
                        .value(4))
                .andExpect(jsonPath("$.totalPages")
                        .value(1));
    }


    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Test
    void updateProduct_ShouldUpdateProduct() throws Exception {

        String requestJson = """
                {
                    "name": "Samsung S25 Ultra Updated",
                    "description": "Updated Samsung phone",
                    "price": 99000,
                    "brand": "Samsung",
                    "categoryId": %d,
                    "stockQuantity": 20,
                    "available": true
                }
                """.formatted(electronics.getId());

        mockMvc.perform(
                        put("/products/{id}", samsung.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Samsung S25 Ultra Updated"))
                .andExpect(jsonPath("$.price")
                        .value(99000.0))
                .andExpect(jsonPath("$.categoryName")
                        .value("Electronics"));
    }


    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Test
    void deleteProduct_ShouldDeleteProduct() throws Exception {

        mockMvc.perform(
                        delete("/products/{id}", samsung.getId())
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get("/products/{id}", samsung.getId())
                )
                .andExpect(status().isNotFound());
    }


    // ---------------------------------------------------------
    // BRAND
    // ---------------------------------------------------------

    @Test
    void getProductsByBrand_ShouldReturnMatchingProducts() throws Exception {

        mockMvc.perform(
                        get("/products/brand/Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Samsung S25 Ultra"));
    }


    // ---------------------------------------------------------
    // PRICE LESS THAN
    // ---------------------------------------------------------

    @Test
    void getProductsLessThanPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/price/less-than/10000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    // ---------------------------------------------------------
    // PRICE GREATER THAN
    // ---------------------------------------------------------

    @Test
    void getProductsGreaterThanPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/price/greater-than/90000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    // ---------------------------------------------------------
    // PRICE LESS THAN OR EQUAL
    // ---------------------------------------------------------

    @Test
    void getProductsLessThanEqualPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/price/less-than-equal/95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }


    // ---------------------------------------------------------
    // PRICE GREATER THAN OR EQUAL
    // ---------------------------------------------------------

    @Test
    void getProductsGreaterThanEqualPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/price/greater-than-equal/95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    // ---------------------------------------------------------
    // AVAILABLE
    // ---------------------------------------------------------

    @Test
    void getAvailableProducts_ShouldReturnAvailableProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/available")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    // ---------------------------------------------------------
    // CATEGORY
    // ---------------------------------------------------------

    @Test
    void getProductsByCategory_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category/Electronics")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    // ---------------------------------------------------------
    // BRAND + AVAILABLE
    // ---------------------------------------------------------

    @Test
    void getProductsByBrandAndAvailable_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/search")
                                .param("brand", "Samsung")
                                .param("available", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Samsung S25 Ultra"));
    }


    // ---------------------------------------------------------
    // SEARCH BY NAME
    // ---------------------------------------------------------

    @Test
    void searchProducts_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/search/name")
                                .param("keyword", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name")
                        .value("Samsung S25 Ultra"));
    }


    // ---------------------------------------------------------
    // CATEGORY + PRICE
    // ---------------------------------------------------------

    @Test
    void getProductsByCategoryAndPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category-price")
                                .param("categoryName", "Electronics")
                                .param("price", "95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // ---------------------------------------------------------
    // CATEGORY + BRAND
    // ---------------------------------------------------------

    @Test
    void getProductsByCategoryAndBrand_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category-brand")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    // ---------------------------------------------------------
    // CATEGORY + BRAND + AVAILABLE
    // ---------------------------------------------------------

    @Test
    void getAvailableProductsByCategoryAndBrand_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category-brand/available")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    // ---------------------------------------------------------
    // CATEGORY + PRICE + AVAILABLE
    // ---------------------------------------------------------

    @Test
    void getAvailableProductsByCategoryAndPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category-price/available")
                                .param("categoryName", "Electronics")
                                .param("price", "95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // ---------------------------------------------------------
    // CATEGORY + BRAND + PRICE
    // ---------------------------------------------------------

    @Test
    void getProductsByCategoryBrandAndPrice_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/category-brand-price")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                                .param("price", "95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // ---------------------------------------------------------
    // ALL FILTERS
    // ---------------------------------------------------------

    @Test
    void getProductsWithAllFilters_ShouldReturnMatchingProducts()
            throws Exception {

        mockMvc.perform(
                        get("/products/filter")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                                .param("price", "95000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}