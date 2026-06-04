package com.mbathegamer.budgetto.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.BudgetRequest;
import com.mbathegamer.budgetto.entities.Budget;
import com.mbathegamer.budgetto.entities.BudgetPeriod;
import com.mbathegamer.budgetto.mappers.BudgetMapper;
import com.mbathegamer.budgetto.repositories.BudgetRepository;
import com.mbathegamer.budgetto.repositories.CategoryRepository;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BudgetService {
  private final UserRepository userRepository;
  private final BudgetRepository budgetRepository;
  private final CategoryRepository categoryRepository;
  private final BudgetMapper mapper;

  public Optional<Budget> create(Long userId, BudgetRequest request) throws Exception {
    var user = userRepository.findById(userId).orElse(null);

    if (user == null) {
      throw new Exception("User not found");
    }

    var category = categoryRepository.findById(request.categoryId()).orElse(null);

    if (category == null) {
      throw new Exception("Category not found");
    }

    var budget = mapper.toEntity(request, user, category);

    budgetRepository.save(budget);

    return Optional.of(budget);
  }

  public List<Budget> findByUserId(Long userId) {
    return budgetRepository.findByUserId(userId);
  }

  public Optional<Budget> findById(Long id, Long userId) {
    var budget = budgetRepository.findById(id).orElse(null);

    if (budget == null || (budget.getUser() != null && budget.getUser().getId() != userId)) {
      return Optional.empty();
    }

    return Optional.of(budget);
  }

  public Optional<Budget> update(Long id, Long userId, BudgetRequest request) {
    var budget = budgetRepository.findById(id).orElse(null);

    if (budget == null || budget.getUser() == null || budget.getUser().getId() != userId) {
      return Optional.empty();
    }

    var category = categoryRepository.findById(request.categoryId()).orElse(null);

    if (category != null) {
      budget.setCategory(category);
    }

    budget.setAmountLimit(BigDecimal.valueOf(request.amountLimit()));
    budget.setPeriod(BudgetPeriod.valueOf(request.period()));
    budget.setStartDate(request.startDate());
    budget.setEndDate(request.endDate());

    return Optional.of(budgetRepository.save(budget));
  }

  public void delete(Long id, Long userId) {
    var budget = budgetRepository.findById(id).orElse(null);

    if (budget != null && budget.getUser() != null && budget.getUser().getId() == userId) {
      budgetRepository.delete(budget);
    }
  }
}
