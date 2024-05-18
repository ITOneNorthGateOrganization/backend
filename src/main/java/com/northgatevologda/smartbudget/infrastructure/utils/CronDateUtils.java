package com.northgatevologda.smartbudget.infrastructure.utils;

import org.springframework.scheduling.support.CronExpression;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Utility class for handling cron expressions and date calculations.
 */
public class CronDateUtils {

    private CronDateUtils() {
    }

    /**
     * Calculates the next execution time based on the given cron expression.
     *
     * @param cron the cron expression to calculate next execution time
     * @return the Instant representing the calculated next execution time in UTC
     */
    public static Instant getNextInstantFromCron(String cron) {
        CronExpression cronExpression = CronExpression.parse(cron);
        LocalDateTime nextExecution = cronExpression.next(LocalDateTime.now());
        if (nextExecution == null) {
            return null;
        }
        return nextExecution.toInstant(ZoneOffset.UTC);
    }
}
