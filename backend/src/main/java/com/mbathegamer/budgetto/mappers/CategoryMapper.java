package com.mbathegamer.budgetto.mappers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mbathegamer.budgetto.dtos.CategoryRequest;
import com.mbathegamer.budgetto.dtos.CategoryResponse;
import com.mbathegamer.budgetto.entities.Category;
import com.mbathegamer.budgetto.entities.CategoryType;
import com.mbathegamer.budgetto.entities.User;

@Component
public class CategoryMapper {
  public CategoryResponse toDto(Category category) {
    var user = category.getUser();

    return new CategoryResponse(
        category.getId(),
        user == null ? "System" : user.getFirstName() + " " + user.getLastName(),
        category.getName(),
        category.getIcon() == null ? "📁" : category.getIcon(),
        category.getType().name(),
        category.isDefault()
    );
  }

  public Category toEntity(CategoryRequest request, User user) {
    return Category.builder()
        .user(user)
        .name(request.name())
        .icon(request.icon() == null ? "📁" : request.icon())
        .type(CategoryType.valueOf(request.type()))
        .isDefault(false)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
