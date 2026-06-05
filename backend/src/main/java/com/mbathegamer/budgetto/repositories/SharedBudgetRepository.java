package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.SharedBudget;

@Repository
public interface SharedBudgetRepository extends JpaRepository<SharedBudget, Long> {
  @Query("SELECT DISTINCT sb FROM SharedBudget sb JOIN sb.members m WHERE m.user.id = :userId")
  List<SharedBudget> findByUserId(Long userId);
}
