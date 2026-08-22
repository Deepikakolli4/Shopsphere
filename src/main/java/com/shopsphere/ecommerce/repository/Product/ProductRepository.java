package com.shopsphere.ecommerce.repository.Product;

import com.shopsphere.ecommerce.entity.Product.Product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> findByBrand(@Param("brand") String brand);

    @Query("SELECT p FROM Product p WHERE p.price < :price")
    List<Product> findByPriceLessThan(@Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p WHERE p.price > :price")
    List<Product> findByPriceGreaterThan(@Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p WHERE p.price <= :price")
    List<Product> findByPriceLessThanEqual(@Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p WHERE p.price >= :price")
    List<Product> findByPriceGreaterThanEqual(@Param("price") BigDecimal price);

    @Query(" SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.available = :available")
    List<Product> findByBrandAndAvailable(@Param("brand") String brand, @Param("available") Boolean available);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    List<Product> findByNameContaining(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.available = true")
    List<Product> findByAvailableTrue();

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.price < :price")
    List<Product> findProductsByCategoryAndPrice(@Param("categoryName") String categoryName, @Param("price") BigDecimal price);

    @Query(" SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.brand = :brand")
    List<Product> findProductsByCategoryAndBrand(@Param("categoryName") String categoryName, @Param("brand") String brand);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.brand = :brand AND p.available = true")
    List<Product> findAvailableProductsByCategoryAndBrand(@Param("categoryName") String categoryName, @Param("brand") String brand);

    @Query(" SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.price < :price AND p.available = true")
    List<Product> findAvailableProductsByCategoryAndPrice(@Param("categoryName") String categoryName, @Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.brand = :brand AND p.price < :price")
    List<Product> findProductsByCategoryBrandAndPrice(@Param("categoryName") String categoryName, @Param("brand") String brand, @Param("price") BigDecimal price);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName AND p.brand = :brand AND p.price < :price AND p.available = true ")
    List<Product> findProductsWithAllFilters(@Param("categoryName") String categoryName, @Param("brand") String brand, @Param("price") BigDecimal price);

    @EntityGraph(attributePaths = "category")
    Page<Product> findAll(Pageable pageable);
}
