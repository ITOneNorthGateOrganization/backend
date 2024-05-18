package com.northgatevologda.smartbudget.infrastructure.tasks;

import com.northgatevologda.smartbudget.domain.ports.in.BudgetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BudgetImplementationTask {
    private static final Logger logger = LoggerFactory.getLogger(BudgetImplementationTask.class);

    private final BudgetService budgetService;

    @Scheduled(cron = "0 0 0 * * *")
    public void executeScheduledTask() {
        try {
            logger.info("Starting execution of scheduled task");
            budgetService.implementSuitableBudgets();
            logger.info("Scheduled task execution completed");
        } catch (Exception e) {
            logger.error("An error occurred while completing the task", e);
        }
    }
}
