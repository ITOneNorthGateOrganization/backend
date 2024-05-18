package com.northgatevologda.smartbudget.application.service.category;

import com.northgatevologda.smartbudget.domain.exception.BadRequestException;
import com.northgatevologda.smartbudget.domain.exception.ForbiddenException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.domain.ports.in.CategoryService;
import com.northgatevologda.smartbudget.domain.ports.in.UserService;
import com.northgatevologda.smartbudget.domain.ports.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link CategoryService}
 */
@RequiredArgsConstructor
@Component
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepositoryPort categoryRepositoryPort;
    private final UserService userService;

    @Override
    public Category save(String username, String category) {
        logger.info("Creating category if not exists for user with username: {} with name: {}", username, category);
        User foundUser = userService.findByUsername(username);
        Category categoryEntity = categoryRepositoryPort.findCategoryByName(category);
        if (categoryEntity == null) {
            Category categoryForSave = Category.builder()
                    .name(category)
                    .forEveryone(false)
                    .build();
            categoryEntity = categoryRepositoryPort.save(categoryForSave);
        } else {
            if (notExistsCategoryByIdForUserId(categoryEntity.getId(), foundUser.getId())) {
                categoryRepositoryPort.tieUpCategoryToUser(categoryEntity.getId(), foundUser.getId());
            } else {
                throw new BadRequestException("Category already exists");
            }
        }
        return categoryEntity;
    }

    @Override
    public List<Category> findCategoriesByUsername(String username) {
        logger.info("Finding all categories for user with username: {}", username);
        User user = userService.findByUsername(username);
        return categoryRepositoryPort.findCategoriesByUserId(user.getId());
    }

    @Override
    public Category findCategoryById(Long id) {
        logger.info("Finding category with ID {}", id);
        return categoryRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    String message = "A category with id: " + id + " not exists";
                    logger.error(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    public void deleteCategory(String username, Long categoryId) {
        logger.info("Deleting category with id: {} for user with username: {}", categoryId, username);
        Category foundCategory = findCategoryById(categoryId);
        User foundUser = userService.findByUsername(username);
        if (notExistsCategoryByIdForUserId(foundCategory.getId(), foundUser.getId())) {
            throw new ForbiddenException("It is forbidden to update someone else's category");
        }
        categoryRepositoryPort.deleteCategory(foundCategory);
    }

    @Override
    public List<Category> findCategoriesByForEveryone(boolean forEveryone) {
        logger.info("Finding categories for everyone");
        return categoryRepositoryPort.findCategoriesByForEveryone(forEveryone);
    }

    @Override
    public boolean notExistsCategoryByIdForUserId(Long id, Long userId) {
        logger.info("Checking if category with ID {} exists for user with ID {}", id, userId);
        return categoryRepositoryPort.notExistsCategoryByIdForUserId(id, userId);
    }
}
