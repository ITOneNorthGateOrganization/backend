package com.northgatevologda.smartbudget.infrastructure.web.controllers.category;

import com.northgatevologda.smartbudget.application.service.category.dto.CategoryDTO;
import com.northgatevologda.smartbudget.application.service.category.mapper.CategoryServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.ports.in.CategoryService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.category.dto.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Category Controller", description = "Controller for managing user-specific categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final CategoryServiceMapper categoryServiceMapper;

    @PostMapping(value = "/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category for the user",
            description = "Creates a new category associated with the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<CategoryDTO> createCategory(
            @Parameter(description = "Category creation request data", required = true)
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating category for user with username: {}", username);
        Category createdCategory = categoryService.save(username, createCategoryRequest.getName());
        return ResponseEntity.ok(categoryServiceMapper.toDTO(createdCategory));
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all categories for the user",
            description = "Retrieves all categories associated with the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<CategoryDTO>> findCategoriesByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding all categories for user with username: {}", username);
        List<Category> categories = categoryService.findCategoriesByUsername(username);
        return ResponseEntity.ok(categoryServiceMapper.toDTOList(categories));
    }

    @DeleteMapping(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a category",
            description = "Deletes a specific category based on the ID for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The expense category has been successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to perform this action"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> deleteCategory(
            @Parameter(description = "The ID of the category to delete", required = true)
            @PathVariable Long categoryId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting category with id: {} for user with username: {}", categoryId, username);
        categoryService.deleteCategory(username, categoryId);
        return ResponseEntity.ok("The expense category has been successfully deleted.");
    }
}
