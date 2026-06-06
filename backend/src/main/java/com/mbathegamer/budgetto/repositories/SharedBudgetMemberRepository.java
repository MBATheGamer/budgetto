package com.mbathegamer.budgetto.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.SharedBudgetMember;

@Repository
public interface SharedBudgetMemberRepository extends JpaRepository<SharedBudgetMember, Long> {
  List<SharedBudgetMember> findBySharedBudgetId(Long sharedBudgetId);
  List<SharedBudgetMember> findByUserId(Long userId);
  Optional<SharedBudgetMember> findBySharedBudgetIdAndUserId(Long id, Long sharedBudgetId,
      Long userId);
}
