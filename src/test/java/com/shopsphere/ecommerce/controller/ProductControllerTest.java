package com.shopsphere.ecommerce.controller;

import com.shopsphere.ecommerce.controller.Product.ProductController;
import com.shopsphere.ecommerce.dto.Product.ProductListResponseDTO;
import com.shopsphere.ecommerce.dto.Product.ProductRequestDTO;
import com.shopsphere.ecommerce.exception.ProductNotFoundException;
import com.shopsphere.ecommerce.service.Product.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;


    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @Test
    void addProduct_ShouldCreateProduct() throws Exception {

        ProductListResponseDTO response = new ProductListResponseDTO();

        when(service.saveProduct(any(ProductRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "Samsung S25 Ultra",
                                    "description": "Premium Samsung phone",
                                    "price": 95000,
                                    "brand": "Samsung",
                                    "stockQuantity": 10,
                                    "available": true,
                                    "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).saveProduct(any(ProductRequestDTO.class));
    }


    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    @Test
    void getAllProducts_ShouldReturnProducts() throws Exception {

        ProductListResponseDTO product = new ProductListResponseDTO();

        PageImpl<ProductListResponseDTO> page =
                new PageImpl<>(
                        List.of(product),
                        PageRequest.of(0, 20),
                        1
                );

        when(service.getAllProducts(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).getAllProducts(any(Pageable.class));
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @Test
    void getProductById_ShouldReturnProduct() throws Exception {

        ProductListResponseDTO product =
                new ProductListResponseDTO();

        when(service.getProductById(1L))
                .thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk());

        verify(service).getProductById(1L);
    }


    // =========================================================
    // GET PRODUCT BY ID - NOT FOUND
    // =========================================================

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound()
            throws Exception {

        when(service.getProductById(999L))
                .thenThrow(
                        new ProductNotFoundException("Product not found")
                );

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());

        verify(service).getProductById(999L);
    }


    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @Test
    void updateProduct_ShouldUpdateProduct() throws Exception {

        ProductListResponseDTO response =
                new ProductListResponseDTO();

        when(service.updateProduct(
                eq(1L),
                any(ProductRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(put("/products/1")
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "Samsung S25 Ultra Updated",
                                    "description": "Updated phone",
                                    "price": 98000,
                                    "brand": "Samsung",
                                    "stockQuantity": 15,
                                    "available": true,
                                    "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).updateProduct(
                eq(1L),
                any(ProductRequestDTO.class)
        );
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @Test
    void deleteProduct_ShouldDeleteProduct() throws Exception {

        doNothing().when(service).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteProduct(1L);
    }


    // =========================================================
    // GET PRODUCTS BY BRAND
    // =========================================================

    @Test
    void getProductsByBrand_ShouldReturnProducts() throws Exception {

        when(service.getProductsByBrand("Samsung"))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(get("/products/brand/Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByBrand("Samsung");
    }


    // =========================================================
    // PRICE LESS THAN
    // =========================================================

    @Test
    void getProductsLessThanPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsLessThanPrice(price))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/price/less-than/50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsLessThanPrice(price);
    }


    // =========================================================
    // PRICE GREATER THAN
    // =========================================================

    @Test
    void getProductsGreaterThanPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsGreaterThanPrice(price))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/price/greater-than/50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsGreaterThanPrice(price);
    }


    // =========================================================
    // PRICE LESS THAN OR EQUAL
    // =========================================================

    @Test
    void getProductsLessThanEqualPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsLessThanEqualPrice(price))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/price/less-than-equal/50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsLessThanEqualPrice(price);
    }


    // =========================================================
    // PRICE GREATER THAN OR EQUAL
    // =========================================================

    @Test
    void getProductsGreaterThanEqualPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsGreaterThanEqualPrice(price))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/price/greater-than-equal/50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsGreaterThanEqualPrice(price);
    }


    // =========================================================
    // AVAILABLE PRODUCTS
    // =========================================================

    @Test
    void getAvailableProducts_ShouldReturnProducts()
            throws Exception {

        when(service.getAvailableProducts())
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(get("/products/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getAvailableProducts();
    }


    // =========================================================
    // PRODUCTS BY CATEGORY
    // =========================================================

    @Test
    void getProductsByCategory_ShouldReturnProducts()
            throws Exception {

        when(service.getProductsByCategory("Electronics"))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category/Electronics")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByCategory("Electronics");
    }


    // =========================================================
    // PRODUCTS BY BRAND AND AVAILABILITY
    // =========================================================

    @Test
    void getProductsByBrandAndAvailable_ShouldReturnProducts()
            throws Exception {

        when(service.getProductsByBrandAndAvailable(
                "Samsung",
                true
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/search")
                                .param("brand", "Samsung")
                                .param("available", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByBrandAndAvailable(
                "Samsung",
                true
        );
    }


    // =========================================================
    // SEARCH BY NAME
    // =========================================================

    @Test
    void searchProducts_ShouldReturnProducts()
            throws Exception {

        when(service.searchProducts("Samsung"))
                .thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/search/name")
                                .param("keyword", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).searchProducts("Samsung");
    }


    // =========================================================
    // CATEGORY + PRICE
    // =========================================================

    @Test
    void getProductsByCategoryAndPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsByCategoryAndPrice(
                "Electronics",
                price
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category-price")
                                .param("categoryName", "Electronics")
                                .param("price", "50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByCategoryAndPrice(
                "Electronics",
                price
        );
    }


    // =========================================================
    // CATEGORY + BRAND
    // =========================================================

    @Test
    void getProductsByCategoryAndBrand_ShouldReturnProducts()
            throws Exception {

        when(service.getProductsByCategoryAndBrand(
                "Electronics",
                "Samsung"
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category-brand")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByCategoryAndBrand(
                "Electronics",
                "Samsung"
        );
    }


    // =========================================================
    // CATEGORY + BRAND + AVAILABLE
    // =========================================================

    @Test
    void getAvailableProductsByCategoryAndBrand_ShouldReturnProducts()
            throws Exception {

        when(service.getAvailableProductsByCategoryAndBrand(
                "Electronics",
                "Samsung"
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category-brand/available")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getAvailableProductsByCategoryAndBrand(
                "Electronics",
                "Samsung"
        );
    }


    // =========================================================
    // CATEGORY + PRICE + AVAILABLE
    // =========================================================

    @Test
    void getAvailableProductsByCategoryAndPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getAvailableProductsByCategoryAndPrice(
                "Electronics",
                price
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category-price/available")
                                .param("categoryName", "Electronics")
                                .param("price", "50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getAvailableProductsByCategoryAndPrice(
                "Electronics",
                price
        );
    }


    // =========================================================
    // CATEGORY + BRAND + PRICE
    // =========================================================

    @Test
    void getProductsByCategoryBrandAndPrice_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsByCategoryBrandAndPrice(
                "Electronics",
                "Samsung",
                price
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/category-brand-price")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                                .param("price", "50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsByCategoryBrandAndPrice(
                "Electronics",
                "Samsung",
                price
        );
    }


    // =========================================================
    // ALL FILTERS
    // =========================================================

    @Test
    void getProductsWithAllFilters_ShouldReturnProducts()
            throws Exception {

        BigDecimal price = new BigDecimal("50000");

        when(service.getProductsWithAllFilters(
                "Electronics",
                "Samsung",
                price
        )).thenReturn(List.of(new ProductListResponseDTO()));

        mockMvc.perform(
                        get("/products/filter")
                                .param("categoryName", "Electronics")
                                .param("brand", "Samsung")
                                .param("price", "50000")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(service).getProductsWithAllFilters(
                "Electronics",
                "Samsung",
                price
        );
    }
}