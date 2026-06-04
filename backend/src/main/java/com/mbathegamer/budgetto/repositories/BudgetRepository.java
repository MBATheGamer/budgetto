package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
  @EntityGraph(attributePaths = "user")
  @Query("SELECT b FROM Budget b WHERE b.user.id = :userId")
  List<Budget> findByUserId(Long userId);
}
