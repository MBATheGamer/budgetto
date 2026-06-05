package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.SharedBudget;

@Repository
public interface SharedBudgetRepository extends JpaRepository<SharedBudget, Long> {
  List<SharedBudget> findByCreatedById(Long userId);
}
