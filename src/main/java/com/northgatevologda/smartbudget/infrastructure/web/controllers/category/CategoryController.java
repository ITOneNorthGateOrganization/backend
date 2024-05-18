package com.northgatevologda.smartbudget.infrastructure.web.controllers.category;

import com.northgatevologda.smartbudget.application.service.category.dto.CategoryDTO;
import com.northgatevologda.smartbudget.application.service.category.mapper.CategoryServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.ports.in.CategoryService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.category.dto.CreateCategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final CategoryServiceMapper categoryServiceMapper;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating category for user with username: {}", username);
        Category createdCategory = categoryService.save(username, createCategoryRequest.getName());
        return ResponseEntity.ok(categoryServiceMapper.toDTO(createdCategory));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding all categories for user with username: {}", username);
        List<Category> categories = categoryService.findCategoriesByUsername(username);
        return ResponseEntity.ok(categoryServiceMapper.toDTOList(categories));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting category with id: {} for user with username: {}", categoryId, username);
        categoryService.deleteCategory(username, categoryId);
        return ResponseEntity.ok("The expense category has been successfully deleted.");
    }
}
