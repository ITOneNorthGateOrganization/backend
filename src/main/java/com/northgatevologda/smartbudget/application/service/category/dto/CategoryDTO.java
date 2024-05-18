package com.northgatevologda.smartbudget.application.service.category.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object representing a category.
 */
@Data
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
}
