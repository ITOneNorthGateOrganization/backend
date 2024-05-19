package com.northgatevologda.smartbudget.infrastructure.jpa.category;

import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.ports.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link CategoryRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaCategoryRepositoryAdapter implements CategoryRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaCategoryRepositoryAdapter.class);

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public List<Category> findCategoriesByBudgetId(Long budgetId) {
        logger.info("Finding categories by budget id: {}", budgetId);
        return jpaCategoryRepository.findCategoriesByBudgetId(budgetId);
    }

    @Override
    public boolean notExistsCategoryByIdForUserId(Long id, Long userId) {
        boolean notExistsCategory = !jpaCategoryRepository.existsCategoryByIdForUserId(id, userId);
        logger.info("Checking if category with ID {} exists for user with ID {}: {}", id, userId, notExistsCategory);
        return notExistsCategory;
    }

    @Override
    public List<Category> findCategoriesByForEveryone(boolean forEveryone) {
        logger.info("Finding findCategories by for everyone: {}", forEveryone);
        return jpaCategoryRepository.findCategoriesByForEveryone(true);
    }

    @Override
    public void tieUpCategoryToUser(Long categoryId, Long userId) {
        logger.info("Tying up category with ID {} to user with ID {}", categoryId, userId);
        jpaCategoryRepository.tieUpCategoryToUser(categoryId, userId);
    }

    @Override
    public Category findCategoryByName(String name) {
        logger.info("Finding category by name: {}", name);
        return jpaCategoryRepository.findCategoryByName(name);
    }

    @Override
    public Category save(Category category) {
        logger.info("Creating category if not exists: {}", category);
        return jpaCategoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoriesByUserId(Long userId) {
        logger.info("Getting all categories for userId: {}", userId);
        return jpaCategoryRepository.findCategoriesByUserId(userId);
    }

    @Override
    public void deleteCategory(Category category) {
        logger.info("Deleting category: {}", category);
        jpaCategoryRepository.delete(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        logger.info("Finding category by id: {}", id);
        return jpaCategoryRepository.findById(id);
    }
}
