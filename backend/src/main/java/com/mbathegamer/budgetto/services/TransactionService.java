package com.mbathegamer.budgetto.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.TransactionRequest;
import com.mbathegamer.budgetto.entities.Transaction;
import com.mbathegamer.budgetto.entities.TransactionType;
import com.mbathegamer.budgetto.mappers.TransactionMapper;
import com.mbathegamer.budgetto.repositories.CategoryRepository;
import com.mbathegamer.budgetto.repositories.SharedBudgetRepository;
import com.mbathegamer.budgetto.repositories.TransactionRepository;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final SharedBudgetRepository sharedBudgetRepository;
  private final TransactionMapper mapper;

  public Optional<Transaction> create(Long userId, TransactionRequest request) {
    var user = userRepository.findById(userId).orElse(null);

    if (user == null) {
      return Optional.empty();
    }

    var category = categoryRepository.findById(request.categoryId()).orElse(null);

    if (category == null) {
      return Optional.empty();
    }

    var sharedBudget = request.sharedBudgetId() != null
        ? sharedBudgetRepository.findById(request.sharedBudgetId()).orElse(null)
        : null;

    var transaction = mapper.toEntity(request, user, category, sharedBudget);

    return Optional.of(transactionRepository.save(transaction));
  }

  public Optional<Transaction> findById(Long id, Long userId) {
    var transaction = transactionRepository.findById(id).orElse(null);

    if (transaction == null || transaction.getUser() == null
        || transaction.getUser().getId() != userId) {
      return Optional.empty();
    }

    return Optional.of(transaction);
  }

  public List<Transaction> findByUser(Long userId) {
    return transactionRepository.findByUserId(userId);
  }

  public List<Transaction> findBySharedBudget(Long sharedBudgetId) {
    return transactionRepository.findBySharedBudgetId(sharedBudgetId);
  }

  public Optional<Transaction> update(Long id, Long userId, TransactionRequest request) {
    var transaction = findById(id, userId).orElse(null);

    var category = categoryRepository.findById(request.categoryId()).orElse(null);

    if (category == null) {
      return Optional.empty();
    }

    transaction.setCategory(category);

    var sharedBudget = sharedBudgetRepository.findById(request.sharedBudgetId()).orElse(null);

    transaction.setSharedBudget(sharedBudget);

    transaction.setType(TransactionType.valueOf(request.type()));
    transaction.setDescription(request.description());
    transaction.setAmount(request.amount());
    transaction.setTransactionDate(request.transactionDate());
    transaction.setComment(request.comment());

    return Optional.of(transactionRepository.save(transaction));
  }

  public void delete(Long id, Long userId) {
    var transaction = findById(id, userId).orElse(null);

    if (transaction != null && transaction.getUser() != null
        && transaction.getUser().getId() == userId) {
      transactionRepository.delete(transaction);
    }
  }
}
