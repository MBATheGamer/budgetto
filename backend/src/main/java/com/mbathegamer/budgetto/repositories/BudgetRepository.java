package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
  List<Budget> findByUserId(Long userId);
}
