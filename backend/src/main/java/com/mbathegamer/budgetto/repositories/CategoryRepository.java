package com.mbathegamer.budgetto.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @EntityGraph(attributePaths = "user")
  @Query("SELECT c FROM Category c WHERE c.user.id = :userId or c.user is null")
  List<Category> findByUserId(Long userId);
}
