package com.shopsphere.ecommerce.service.Product;

import com.shopsphere.ecommerce.exception.CategoryNotFoundException;
import com.shopsphere.ecommerce.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shopsphere.ecommerce.dto.Product.ProductListResponseDTO;
import com.shopsphere.ecommerce.dto.Product.ProductRequestDTO;
import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import com.shopsphere.ecommerce.repository.Product.ProductRepository;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import com.shopsphere.ecommerce.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductListResponseDTO saveProduct(ProductRequestDTO dto) {
        log.info("Creating product with name: {}", dto.getName());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        Product product = mapper.toEntity(dto, category);
        Product savedProduct = repository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());
        return mapper.toProductListDTO(savedProduct);
    }

    public Page<ProductListResponseDTO> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toProductListDTO);
    }

    public ProductListResponseDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product with id {} not found", id);
                    return new ProductNotFoundException("Product not found");
                });
        return mapper.toProductListDTO(product);
    }

    public ProductListResponseDTO updateProduct(Long id,
                                                ProductRequestDTO dto) {

        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found"));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setBrand(dto.getBrand());
        existingProduct.setStockQuantity(dto.getStockQuantity());
        existingProduct.setAvailable(dto.getAvailable());
        existingProduct.setCategory(category);

        Product updatedProduct = repository.save(existingProduct);

        return mapper.toProductListDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        repository.delete(product);
    }

    public List<ProductListResponseDTO> getProductsByBrand(String brand) {

        return repository.findByBrand(brand)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsLessThanPrice(BigDecimal price){
        return repository.findByPriceLessThan(price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsGreaterThanPrice(BigDecimal price){
        return repository.findByPriceGreaterThan(price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsLessThanEqualPrice(BigDecimal price){
        return repository.findByPriceLessThanEqual(price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsGreaterThanEqualPrice(BigDecimal price){
        return repository.findByPriceGreaterThanEqual(price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getAvailableProducts(){
        return repository.findByAvailableTrue()
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsByCategory(String categoryName){
        return repository.findByCategoryName(categoryName)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsByBrandAndAvailable(String brand, Boolean available){
        return repository.findByBrandAndAvailable(brand, available)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> searchProducts(String keyword){
        return repository.findByNameContaining(keyword)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsByCategoryAndPrice(String categoryName, BigDecimal price) {
        return repository.findProductsByCategoryAndPrice(categoryName, price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsByCategoryAndBrand(String categoryName, String brand) {
        return repository.findProductsByCategoryAndBrand(categoryName, brand)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getAvailableProductsByCategoryAndBrand(String categoryName, String brand) {
        return repository.findAvailableProductsByCategoryAndBrand(categoryName, brand)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getAvailableProductsByCategoryAndPrice(String categoryName, BigDecimal price) {
        return repository.findAvailableProductsByCategoryAndPrice(categoryName, price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }
    public List<ProductListResponseDTO> getProductsByCategoryBrandAndPrice(
            String categoryName,
            String brand,
            BigDecimal price) {

        return repository.findProductsByCategoryBrandAndPrice(categoryName, brand, price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

    public List<ProductListResponseDTO> getProductsWithAllFilters(
            String categoryName,
            String brand,
            BigDecimal price) {

        return repository.findProductsWithAllFilters(categoryName, brand, price)
                .stream()
                .map(mapper::toProductListDTO)
                .toList();
    }

}