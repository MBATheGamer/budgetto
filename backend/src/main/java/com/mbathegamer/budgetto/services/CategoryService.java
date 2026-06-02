package com.mbathegamer.budgetto.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.CategoryRequest;
import com.mbathegamer.budgetto.entities.Category;
import com.mbathegamer.budgetto.mappers.CategoryMapper;
import com.mbathegamer.budgetto.repositories.CategoryRepository;
import com.mbathegamer.budgetto.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.var;

@Service
@AllArgsConstructor
public class CategoryService {
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final CategoryMapper mapper;

  public Optional<Category> create(Long userId, CategoryRequest request) throws Exception {
    var user = userRepository.findById(userId).orElse(null);

    if (user == null) {
      throw new Exception("User not found");
    }

    var category = mapper.toEntity(request, user);

    categoryRepository.save(category);

    return Optional.of(category);
  }
}
