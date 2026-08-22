package com.shopsphere.ecommerce.service;

import com.shopsphere.ecommerce.dto.Product.ProductListResponseDTO;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.exception.CategoryNotFoundException;
import com.shopsphere.ecommerce.exception.ProductNotFoundException;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.mapper.ProductMapper;
import com.shopsphere.ecommerce.dto.Product.ProductRequestDTO;
import com.shopsphere.ecommerce.entity.Category.Category;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.shopsphere.ecommerce.service.Product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService service;

    @Test
    void getProductById_WhenProductExists_ReturnsProductDTO() {

        // Arrange
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Samsung S25 Ultra");

        ProductListResponseDTO dto = new ProductListResponseDTO();
        dto.setId(productId);
        dto.setName("Samsung S25 Ultra");

        when(repository.findById(productId))
                .thenReturn(Optional.of(product));

        when(mapper.toProductListDTO(product))
                .thenReturn(dto);

        // Act
        ProductListResponseDTO result =
                service.getProductById(productId);

        // Assert
        assertEquals(productId, result.getId());
        assertEquals("Samsung S25 Ultra", result.getName());
    }
    @Test
    void getProductById_WhenProductDoesNotExist_ThrowsException() {

        // Arrange
        Long productId = 999L;

        when(repository.findById(productId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> service.getProductById(productId)
                );

        assertEquals("Product not found", exception.getMessage());
    }
    @Test
    void saveProduct_WhenValidRequest_ReturnsProductDTO() {

        // Arrange
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Samsung S25 Ultra");
        request.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Samsung S25 Ultra");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Samsung S25 Ultra");

        ProductListResponseDTO response = new ProductListResponseDTO();
        response.setId(1L);
        response.setName("Samsung S25 Ultra");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(mapper.toEntity(request, category))
                .thenReturn(product);

        when(repository.save(product))
                .thenReturn(savedProduct);

        when(mapper.toProductListDTO(savedProduct))
                .thenReturn(response);

        // Act
        ProductListResponseDTO result =
                service.saveProduct(request);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Samsung S25 Ultra", result.getName());
    }
    @Test
    void updateProduct_WhenProductExists_ReturnsUpdatedProductDTO() {

        // Arrange
        Long productId = 1L;

        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Samsung S25 Ultra Updated");
        request.setDescription("Updated description");
        request.setPrice(new BigDecimal("100000"));
        request.setBrand("Samsung");
        request.setStockQuantity(20);
        request.setAvailable(true);
        request.setCategoryId(1L);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Samsung S25 Ultra");

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        ProductListResponseDTO response = new ProductListResponseDTO();
        response.setId(productId);
        response.setName("Samsung S25 Ultra Updated");

        when(repository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(existingProduct))
                .thenReturn(existingProduct);

        when(mapper.toProductListDTO(existingProduct))
                .thenReturn(response);

        // Act
        ProductListResponseDTO result =
                service.updateProduct(productId, request);

        // Assert
        assertEquals(productId, result.getId());
        assertEquals("Samsung S25 Ultra Updated", result.getName());

        assertEquals("Samsung S25 Ultra Updated",
                existingProduct.getName());

        assertEquals(new BigDecimal("100000"),
                existingProduct.getPrice());

        assertEquals("Samsung",
                existingProduct.getBrand());

        assertEquals(20,
                existingProduct.getStockQuantity());

        assertEquals(true,
                existingProduct.getAvailable());

        assertEquals(category,
                existingProduct.getCategory());
    }
    @Test
    void updateProduct_WhenProductDoesNotExist_ThrowsException() {

        // Arrange
        Long productId = 999L;

        ProductRequestDTO request = new ProductRequestDTO();
        request.setCategoryId(1L);

        when(repository.findById(productId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> service.updateProduct(productId, request)
                );

        assertEquals("Product not found",
                exception.getMessage());
    }
    @Test
    void updateProduct_WhenCategoryDoesNotExist_ThrowsException() {

        // Arrange
        Long productId = 1L;

        ProductRequestDTO request = new ProductRequestDTO();
        request.setCategoryId(999L);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Samsung S25 Ultra");

        when(repository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception =
                assertThrows(
                        CategoryNotFoundException.class,
                        () -> service.updateProduct(productId, request)
                );

        assertEquals("Category not found",
                exception.getMessage());
    }
    @Test
    void deleteProduct_WhenProductExists_DeletesProduct() {

        // Arrange
        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setName("Samsung S25 Ultra");

        when(repository.findById(productId))
                .thenReturn(Optional.of(product));

        // Act
        service.deleteProduct(productId);

        // Assert
        verify(repository).delete(product);
    }
    @Test
    void deleteProduct_WhenProductDoesNotExist_ThrowsException() {

        // Arrange
        Long productId = 999L;

        when(repository.findById(productId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> service.deleteProduct(productId)
                );

        assertEquals("Product not found",
                exception.getMessage());

        verify(repository, never()).delete(any());
    }
}