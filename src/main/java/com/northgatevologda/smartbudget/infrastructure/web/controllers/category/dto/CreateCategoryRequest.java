package com.northgatevologda.smartbudget.infrastructure.web.controllers.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a request object for creating a category.
 */
@Data
public class CreateCategoryRequest {
    @NotNull
    private String name;
}
