package com.shopsphere.ecommerce.controller.Category;

import com.shopsphere.ecommerce.entity.Category.Category;
import com.shopsphere.ecommerce.service.Category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category) {

        Category savedCategory = service.saveCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {

        return ResponseEntity.ok(service.getAllCategories());
    }
}
