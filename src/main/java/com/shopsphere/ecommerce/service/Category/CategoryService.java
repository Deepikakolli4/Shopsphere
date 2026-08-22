package com.shopsphere.ecommerce.service.Category;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.repository.Category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }
}
