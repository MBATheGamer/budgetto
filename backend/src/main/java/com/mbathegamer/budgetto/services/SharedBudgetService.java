package com.mbathegamer.budgetto.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.SharedBudgetRequest;
import com.mbathegamer.budgetto.entities.SharedBudget;
import com.mbathegamer.budgetto.entities.SharedBudgetPeriodType;
import com.mbathegamer.budgetto.mappers.SharedBudgetMapper;
import com.mbathegamer.budgetto.repositories.SharedBudgetRepository;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SharedBudgetService {
  private final UserRepository userRepository;
  private final SharedBudgetRepository sharedBudgetRepository;
  private final SharedBudgetMapper mapper;

  public Optional<SharedBudget> create(Long userId, SharedBudgetRequest request) throws Exception {
    var user = userRepository.findById(userId).orElse(null);

    if (user == null) {
      throw new Exception("User not found");
    }

    var sharedBudget = mapper.toEntity(request, user);

    sharedBudgetRepository.save(sharedBudget);

    return Optional.of(sharedBudget);
  }

  public List<SharedBudget> findByUserId(Long userId) {
    return sharedBudgetRepository.findByUserId(userId);
  }

  public Optional<SharedBudget> findById(Long id, Long userId) {
    var sharedBudget = sharedBudgetRepository.findById(id).orElse(null);

    if (sharedBudget == null
        || (sharedBudget.getCreatedBy() != null && sharedBudget.getCreatedBy().getId() != userId)) {
      return Optional.empty();
    }

    return Optional.of(sharedBudget);
  }

  public Optional<SharedBudget> update(Long id, Long userId, SharedBudgetRequest request) {
    var sharedBudget = sharedBudgetRepository.findById(id).orElse(null);

    if (sharedBudget == null || sharedBudget.getCreatedBy() == null
        || sharedBudget.getCreatedBy().getId() != userId) {
      return Optional.empty();
    }

    sharedBudget.setName(request.name());
    sharedBudget.setAmountLimit(BigDecimal.valueOf(request.amountLimit()));
    sharedBudget.setPeriodType(SharedBudgetPeriodType.valueOf(request.periodType()));
    sharedBudget.setStartDate(request.startDate());
    sharedBudget.setEndDate(request.endDate());

    return Optional.of(sharedBudgetRepository.save(sharedBudget));
  }

  public void delete(Long id, Long userId) {
    var budget = sharedBudgetRepository.findById(id).orElse(null);

    if (budget != null && budget.getCreatedBy() != null
        && budget.getCreatedBy().getId() == userId) {
      sharedBudgetRepository.delete(budget);
    }
  }
}
