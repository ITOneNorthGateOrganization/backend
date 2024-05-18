package com.northgatevologda.smartbudget.application.service.category.mapper;

import com.northgatevologda.smartbudget.application.service.category.dto.CategoryDTO;
import com.northgatevologda.smartbudget.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper class for CategoryService that converts entities to DTO objects.
 */
@Component
public class CategoryServiceMapper {

    /**
     * Converts Category entity to CategoryDTO data transfer object.
     *
     * @param category the Category entity
     * @return the CategoryDTO object
     */
    public CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    /**
     * Converts a list of Category entities to a list of CategoryDTOs.
     *
     * @param categories The list of Category entities to convert.
     * @return A list of CategoryDTO objects representing the categories.
     */
    public List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream().map(this::toDTO).toList();
    }

    /**
     * Converts CategoryDTO data transfer object to Category entity.
     *
     * @param categoryDTO the CategoryDTO object
     * @return the Category entity
     */
    public Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .build();
    }

    /**
     * Converts a list of CategoryDTO objects to a list of Category entities.
     *
     * @param categoryDTOs the list of CategoryDTO objects
     * @return a list of Category entities
     */
    public List<Category> toEntityList(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream()
                .map(this::toEntity)
                .toList();
    }
}
