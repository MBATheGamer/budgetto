package com.mbathegamer.budgetto.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "shared_budgets")
public class SharedBudget {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "created_by")
  private User createdBy;

  @Column(name = "name")
  private String name;

  @Column(name = "amount_limit")
  private BigDecimal amountLimit;

  @Column(name = "period_type")
  @Enumerated(EnumType.STRING)
  private SharedBudgetPeriodType periodType;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Builder.Default
  @OneToMany(mappedBy = "sharedBudget")
  private List<SharedBudgetMember> members = new ArrayList<>();
}
