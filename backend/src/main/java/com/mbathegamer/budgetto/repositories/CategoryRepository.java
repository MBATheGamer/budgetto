package com.mbathegamer.budgetto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
