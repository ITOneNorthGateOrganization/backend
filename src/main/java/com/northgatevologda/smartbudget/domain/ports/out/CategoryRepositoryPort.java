package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * This port interface defines methods for interacting with a category repository.
 */
public interface CategoryRepositoryPort {

    /**
     * Find categories based on visibility for everyone.
     *
     * @param forEveryone Boolean value indicating if the categories are set for everyone.
     * @return A list of categories based on the visibility.
     */
    List<Category> findCategoriesByForEveryone(boolean forEveryone);

    /**
     * Tie up a category with a specific user.
     *
     * @param categoryId The ID of the category.
     * @param userId     The ID of the user to tie up the category with.
     */
    void tieUpCategoryToUser(Long categoryId, Long userId);

    /**
     * Find a category by its name.
     *
     * @param name The name of the category to find.
     * @return The category found, or null if not found.
     */
    Category findCategoryByName(String name);

    /**
     * Save a category.
     *
     * @param category The category to save.
     * @return The saved category.
     */
    Category save(Category category);

    /**
     * Find categories by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of categories associated with the user ID.
     */
    List<Category> findCategoriesByUserId(Long userId);

    /**
     * Delete a category.
     *
     * @param category The category to delete.
     */
    void deleteCategory(Category category);

    /**
     * Find a category by its ID.
     *
     * @param id The ID of the category to find.
     * @return An Optional containing the category found, or empty if not found.
     */
    Optional<Category> findById(Long id);

    /**
     * Check if a category with the specified ID exists for the given user ID.
     *
     * @param id     The ID of the category to check.
     * @param userId The ID of the user to check against.
     * @return True if the category does not exist for the user, false otherwise.
     */
    boolean notExistsCategoryByIdForUserId(Long id, Long userId);
}