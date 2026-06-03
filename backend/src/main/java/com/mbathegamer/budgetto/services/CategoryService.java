package com.mbathegamer.budgetto.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mbathegamer.budgetto.dtos.CategoryRequest;
import com.mbathegamer.budgetto.entities.Category;
import com.mbathegamer.budgetto.entities.CategoryType;
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

  public List<Category> findByUserId(Long userId) {
    return categoryRepository.findByUserId(userId);
  }

  public Optional<Category> findById(Long id, Long userId) {
    var category = categoryRepository.findById(id).orElse(null);

    if (category == null || (category.getUser() != null && category.getUser().getId() != userId)) {
      return Optional.empty();
    }

    return Optional.of(category);
  }

  public Optional<Category> update(Long id, Long userId, CategoryRequest request) {
    var category = categoryRepository.findById(id).orElse(null);

    if (category == null || category.getUser() == null || category.getUser().getId() != userId) {
      return Optional.empty();
    }

    category.setName(request.name());
    category.setIcon(request.icon());
    category.setType(CategoryType.valueOf(request.type()));

    return Optional.of(categoryRepository.save(category));
  }
}
