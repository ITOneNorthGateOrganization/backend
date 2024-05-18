package com.northgatevologda.smartbudget.application.service.budget.mapper;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.domain.model.Budget;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is responsible for mapping Budget entities to BudgetDTOs.
 */
@Component
public class BudgetServiceMapper {

    /**
     * Maps BudgetDTO to Budget entity.
     *
     * @param budgetDTO The BudgetDTO to map.
     * @return Budget entity containing the ID, limit, purpose, importance, and periodic from the BudgetDTO.
     */
    public Budget toEntity(BudgetDTO budgetDTO) {
        return Budget.builder()
                .id(budgetDTO.getId())
                .limit(budgetDTO.getLimit())
                .purpose(budgetDTO.getPurpose())
                .importance(budgetDTO.getImportance())
                .cron(budgetDTO.getCron())
                .build();
    }

    /**
     * Maps Budget entity to BudgetDTO.
     *
     * @param budget The Budget entity to map.
     * @return BudgetDTO containing the ID, limit, importance, periodic, and purpose from the Budget entity.
     */
    public BudgetDTO toDTO(Budget budget) {
        return BudgetDTO.builder()
                .id(budget.getId())
                .limit(budget.getLimit())
                .importance(budget.getImportance())
                .cron(budget.getCron())
                .purpose(budget.getPurpose())
                .build();
    }

    /**
     * Maps a list of Budget entities to a list of BudgetDTOs.
     *
     * @param budgets The list of Budget entities to map.
     * @return List of BudgetDTOs containing the details of each budget in the list.
     */
    public List<BudgetDTO> toDTOList(List<Budget> budgets) {
        return budgets.stream().map(this::toDTO).toList();
    }
}
