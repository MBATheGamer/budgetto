package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.SharedBudget;

@Repository
public interface SharedBudgetRepository extends JpaRepository<SharedBudget, Long> {
  @EntityGraph(attributePaths = "createdBy")
  @Query("SELECT sb FROM SharedBudget sb WHERE sb.createdBy.id = :userId")
  List<SharedBudget> findByUserId(Long userId);
}
