package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.domain.model.Category;

import java.util.List;

/**
 * This interface defines methods for managing categories.
 */
public interface CategoryService {

    /**
     * Save a new category for the given user.
     *
     * @param username The username of the user for whom to save the category.
     * @param category The name of the category to save.
     * @return The saved category.
     */
    Category save(String username, String category);

    /**
     * Find a category by its ID.
     *
     * @param id The ID of the category to find.
     * @return The category found, or null if not found.
     */
    Category findCategoryById(Long id);

    /**
     * Find all categories associated with the given username.
     *
     * @param username The username of the user whose categories to find.
     * @return A list of categories associated with the given username.
     */
    List<Category> findCategoriesByUsername(String username);

    /**
     * Delete a category with the given ID for the specified user.
     *
     * @param username   The username of the user who owns the category to be deleted.
     * @param categoryId The ID of the category to be deleted.
     */
    void deleteCategory(String username, Long categoryId);

    /**
     * Find all categories that are set as public or private.
     *
     * @param forEveryone Boolean value indicating if the categories are set for everyone or not.
     * @return A list of categories based on the visibility.
     */
    List<Category> findCategoriesByForEveryone(boolean forEveryone);

    /**
     * Check if a category with the specified ID exists for the given user ID.
     *
     * @param id     The ID of the category to check.
     * @param userId The ID of the user to check against.
     * @return True if the category does not exist for the user, false otherwise.
     */
    boolean notExistsCategoryByIdForUserId(Long id, Long userId);
}
