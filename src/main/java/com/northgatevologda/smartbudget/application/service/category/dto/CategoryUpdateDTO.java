package com.northgatevologda.smartbudget.application.service.category.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object for updating a category.
 */
@Data
@Builder
public class CategoryUpdateDTO {
    private Long id;
    private String name;
}
