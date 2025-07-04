package com.personal.finance.tracker.demo.category.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.finance.tracker.demo.category.data.Category;
import com.personal.finance.tracker.demo.category.logic.CategoryService;
import com.personal.finance.tracker.demo.category.web.bodies.CategoryResponse;
import com.personal.finance.tracker.demo.category.web.bodies.CreateCategoryRequest;
import com.personal.finance.tracker.demo.category.web.bodies.RenameCategoryRequest;
import com.personal.finance.tracker.demo.exception.NotFoundException;
import com.personal.finance.tracker.demo.user.data.User;
import com.personal.finance.tracker.demo.user.logic.UserProviderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserProviderService userProviderService;

    public CategoryController(CategoryService categoryService, UserProviderService userProviderService) {
        this.categoryService = categoryService;
        this.userProviderService = userProviderService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") UUID id) throws NotFoundException {
        return ResponseEntity.ok(new CategoryResponse(categoryService.getCategoryById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> renameCategory(@PathVariable("id") UUID id, @RequestBody RenameCategoryRequest request) throws NotFoundException  {
        Category category = categoryService.renameCategory(id, request.getName());
        return ResponseEntity.ok(new CategoryResponse(category));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() throws NotFoundException {
        User appUser = userProviderService.getCurrentUser().orElseThrow(() -> new NotFoundException("User not found"));
        return ResponseEntity.ok(categoryService.getAllCategories(appUser).stream()
                .map(CategoryResponse::new)
            .collect((Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CreateCategoryRequest request) throws NotFoundException {
        User appUser = userProviderService.getCurrentUser().orElseThrow(() -> new NotFoundException("User not found"));

        Category savedCategory = categoryService.saveCategory(request.getCategory(), appUser);
        return ResponseEntity.ok(new CategoryResponse(savedCategory));
    }

      @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID id) throws NotFoundException {
        User appUser = userProviderService.getCurrentUser().orElseThrow(() -> new NotFoundException("User not found"));
        categoryService.deleteCategory(id, appUser);
        return ResponseEntity.ok().build();
    }
    
}
