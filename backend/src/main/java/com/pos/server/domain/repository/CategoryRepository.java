package com.pos.server.domain.repository;

import com.pos.server.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> getAll();
    Optional<Category> getCategory(int categoryID);

    Category save(Category category);
}
