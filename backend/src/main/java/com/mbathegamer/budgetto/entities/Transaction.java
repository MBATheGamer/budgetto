package com.mbathegamer.budgetto.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @JoinColumn(name = "category_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shared_budget_id")
  private SharedBudget sharedBudget;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "amount")
  private BigDecimal amount;

  @Column(name = "transaction_date")
  private LocalDate transactionDate;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @Column(name = "create_at")
  private LocalDateTime createAt;
}
