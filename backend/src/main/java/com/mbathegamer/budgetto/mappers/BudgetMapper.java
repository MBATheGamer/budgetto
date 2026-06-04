package com.mbathegamer.budgetto.mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.BudgetRequest;
import com.mbathegamer.budgetto.dtos.BudgetResponse;
import com.mbathegamer.budgetto.entities.Budget;
import com.mbathegamer.budgetto.entities.BudgetPeriod;
import com.mbathegamer.budgetto.entities.Category;
import com.mbathegamer.budgetto.entities.User;

@Component
public class BudgetMapper {
  public BudgetResponse toDto(Budget budget) {
    var category =
        String.format("%s %s", budget.getCategory().getIcon(), budget.getCategory().getName());

    return new BudgetResponse(
        budget.getId(),
        budget.getCategory().getId(),
        category,
        budget.getPeriod().name().toLowerCase(),
        budget.getAmountLimit().doubleValue(),
        budget.getStartDate(),
        budget.getEndDate(),
        budget.getAlertThreshold()
    );
  }

  public Budget toEntity(BudgetRequest request, User user, Category category) throws Exception {
    return Budget.builder()
        .user(user)
        .category(category)
        .amountLimit(BigDecimal.valueOf(request.amountLimit()))
        .period(BudgetPeriod.valueOf(request.period()))
        .startDate(request.startDate())
        .endDate(request.endDate())
        .alertThreshold(request.alertThreshold())
        .createdAt(LocalDateTime.now())
        .build();
  }
}
