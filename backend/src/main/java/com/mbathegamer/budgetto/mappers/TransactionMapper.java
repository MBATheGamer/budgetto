package com.mbathegamer.budgetto.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.TransactionRequest;
import com.mbathegamer.budgetto.dtos.TransactionResponse;
import com.mbathegamer.budgetto.entities.Category;
import com.mbathegamer.budgetto.entities.SharedBudget;
import com.mbathegamer.budgetto.entities.Transaction;
import com.mbathegamer.budgetto.entities.TransactionType;
import com.mbathegamer.budgetto.entities.User;

@Component
public class TransactionMapper {
  public TransactionResponse toDto(Transaction transaction) {
    return new TransactionResponse(
        transaction.getId(),
        transaction.getTransactionDate(),
        transaction.getDescription(),
        transaction.getCategory().getName(),
        transaction.getUser().getFirstName(),
        transaction.getUser().getLastName(),
        transaction.getType().name(),
        transaction.getAmount().doubleValue()
    );
  }

  public Transaction toEntity(TransactionRequest request, User user, Category category,
      SharedBudget sharedBudget) {
    return Transaction.builder()
        .user(user)
        .category(category)
        .sharedBudget(sharedBudget)
        .type(TransactionType.valueOf(request.type()))
        .description(request.description())
        .amount(request.amount())
        .transactionDate(request.transactionDate())
        .comment(request.comment())
        .createAt(LocalDateTime.now())
        .build();
  }
}
