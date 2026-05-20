package com.mbathegamer.budgetto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbathegamer.budgetto.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
