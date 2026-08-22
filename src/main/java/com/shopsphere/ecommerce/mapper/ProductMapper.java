package com.shopsphere.ecommerce.mapper;

import com.shopsphere.ecommerce.dto.Product.ProductListResponseDTO;
import com.shopsphere.ecommerce.dto.Product.ProductRequestDTO;
import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.entity.Product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductListResponseDTO toProductListDTO(Product product) {

        return new ProductListResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName()
        );
    }
    public Product toEntity(ProductRequestDTO dto, Category category) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setStockQuantity(dto.getStockQuantity());
        product.setAvailable(dto.getAvailable());

        product.setCategory(category);

        return product;
    }
}
