package com.mbathegamer.budgetto.mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.SharedBudgetRequest;
import com.mbathegamer.budgetto.entities.SharedBudget;
import com.mbathegamer.budgetto.entities.SharedBudgetPeriodType;
import com.mbathegamer.budgetto.entities.User;

@Component
public class SharedBudgetMapper {
  public SharedBudget toEntity(SharedBudgetRequest request, User user) {
    return SharedBudget.builder()
        .createdBy(user)
        .name(request.name())
        .amountLimit(BigDecimal.valueOf(request.amountLimit()))
        .periodType(SharedBudgetPeriodType.valueOf(request.periodType()))
        .startDate(request.startDate())
        .endDate(request.endDate())
        .createdAt(LocalDateTime.now())
        .build();
  }
}
