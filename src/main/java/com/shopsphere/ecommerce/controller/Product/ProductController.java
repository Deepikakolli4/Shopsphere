package com.shopsphere.ecommerce.controller.Product;

import com.shopsphere.ecommerce.dto.Product.ProductListResponseDTO;
import com.shopsphere.ecommerce.dto.Product.ProductRequestDTO;
import com.shopsphere.ecommerce.service.Product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @Operation(
            summary = "Create a product",
            description = "Creates a new product"
    )
    @PostMapping
    public ProductListResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO dto) {
        return service.saveProduct(dto);
    }

    @Operation(summary = "Get all products",
            description = "Returns products with pagination and sorting")
    @GetMapping
    public ResponseEntity<Page<ProductListResponseDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(service.getAllProducts(pageable));
    }

    @Operation(
            summary = "Get product by ID",
            description = "Returns a product using its unique ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductListResponseDTO> getProductById(@PathVariable Long id){
        ProductListResponseDTO product =  service.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Updates a product by Id",
            description = "Returns a new updated product"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductListResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO dto) {

        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

    @Operation(
            summary = "Deletes a product",
            description = "Removes product"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get products by brand",
            description = "Returns products belonging to the specified brand"
    )
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByBrand(@PathVariable String brand){
        return ResponseEntity.ok(service.getProductsByBrand(brand));
    }

    @Operation(
            summary = "Get products below a price",
            description = "Returns products with a price less than the specified value"
    )
    @GetMapping("/price/less-than/{price}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsLessThanPrice(@PathVariable BigDecimal price){
        return ResponseEntity.ok(service.getProductsLessThanPrice(price));
    }

    @Operation(
            summary = "Get products above a price",
            description = "Returns products with a price greater than the specified value"
    )
    @GetMapping("/price/greater-than/{price}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsGreaterThanPrice(@PathVariable BigDecimal price){
        return ResponseEntity.ok(service.getProductsGreaterThanPrice(price));
    }

    @Operation(
            summary = "Get products at or below a price",
            description = "Returns products with a price less than or equal to the specified value"
    )
    @GetMapping("/price/less-than-equal/{price}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsLessThanEqualPrice(
            @PathVariable BigDecimal price) {

        return ResponseEntity.ok(service.getProductsLessThanEqualPrice(price));
    }

    @Operation(
            summary = "Get products at or above a price",
            description = "Returns products with a price greater than or equal to the specified value"
    )
    @GetMapping("/price/greater-than-equal/{price}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsGreaterThanEqualPrice(
            @PathVariable BigDecimal price) {

        return ResponseEntity.ok(service.getProductsGreaterThanEqualPrice(price));
    }

    @Operation(
            summary = "Get available products",
            description = "Returns all currently available products"
    )
    @GetMapping("/available")
    public ResponseEntity<List<ProductListResponseDTO>> getAvailableProducts() {

        return ResponseEntity.ok(service.getAvailableProducts());
    }

    @Operation(
            summary = "Get products by category",
            description = "Returns products belonging to the specified category"
    )
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByCategory(
            @PathVariable String categoryName) {

        return ResponseEntity.ok(service.getProductsByCategory(categoryName));
    }

    @Operation(
            summary = "Search products by brand and availability",
            description = "Returns products matching the specified brand and availability"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByBrandAndAvailable(
            @RequestParam String brand,
            @RequestParam Boolean available) {

        return ResponseEntity.ok(
                service.getProductsByBrandAndAvailable(brand, available)
        );
    }

    @Operation(
            summary = "Search products by name",
            description = "Returns products whose name contains the specified keyword"
    )
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductListResponseDTO>> searchProducts(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                service.searchProducts(keyword)
        );
    }

    @Operation(
            summary = "Filter products by category and price",
            description = "Returns products matching the specified category and price"
    )
    @GetMapping("/category-price")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByCategoryAndPrice(
            @RequestParam String categoryName,
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getProductsByCategoryAndPrice(categoryName, price)
        );
    }

    @Operation(
            summary = "Filter products by category and brand",
            description = "Returns products matching the specified category and brand"
    )
    @GetMapping("/category-brand")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByCategoryAndBrand(
            @RequestParam String categoryName,
            @RequestParam String brand) {

        return ResponseEntity.ok(
                service.getProductsByCategoryAndBrand(categoryName, brand)
        );
    }

    @Operation(
            summary = "Filter available products by category and brand",
            description = "Returns available products matching the specified category and brand"
    )
    @GetMapping("/category-brand/available")
    public ResponseEntity<List<ProductListResponseDTO>> getAvailableProductsByCategoryAndBrand(
            @RequestParam String categoryName,
            @RequestParam String brand) {

        return ResponseEntity.ok(
                service.getAvailableProductsByCategoryAndBrand(categoryName, brand)
        );
    }

    @Operation(
            summary = "Filter available products by category and price",
            description = "Returns available products matching the specified category and price"
    )
    @GetMapping("/category-price/available")
    public ResponseEntity<List<ProductListResponseDTO>> getAvailableProductsByCategoryAndPrice(
            @RequestParam String categoryName,
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getAvailableProductsByCategoryAndPrice(categoryName, price)
        );
    }
    @Operation(
            summary = "Filter products by category, brand and price",
            description = "Returns products matching the specified category, brand and price"
    )
    @GetMapping("/category-brand-price")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsByCategoryBrandAndPrice(
            @RequestParam String categoryName,
            @RequestParam String brand,
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getProductsByCategoryBrandAndPrice(
                        categoryName, brand, price
                )
        );
    }
    @Operation(
            summary = "Filter products using all criteria",
            description = "Returns products matching the specified category, brand and price"
    )
    @GetMapping("/filter")
    public ResponseEntity<List<ProductListResponseDTO>> getProductsWithAllFilters(
            @RequestParam String categoryName,
            @RequestParam String brand,
            @RequestParam BigDecimal price) {

        return ResponseEntity.ok(
                service.getProductsWithAllFilters(
                        categoryName, brand, price
                )
        );
    }
}
