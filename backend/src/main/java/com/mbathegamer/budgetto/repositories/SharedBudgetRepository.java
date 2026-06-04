package com.mbathegamer.budgetto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedBudgetRepository extends JpaRepository<SharedBudgetRepository, Long> {}
