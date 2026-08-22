package com.shopsphere.ecommerce.repository.Category;

import com.shopsphere.ecommerce.entity.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
