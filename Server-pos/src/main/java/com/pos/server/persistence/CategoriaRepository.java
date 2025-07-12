package com.pos.server.persistence;

import com.pos.server.domain.Category;
import com.pos.server.domain.repository.CategoryRepository;
import com.pos.server.persistence.crud.CategoriaCrudRepository;
import com.pos.server.persistence.entity.Categoria;
import com.pos.server.persistence.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepository implements CategoryRepository {
    @Autowired
    private CategoryMapper mapper;
    @Autowired
    private CategoriaCrudRepository categoriaCrudRepository;

    @Override
    public List<Category> getAll() {
        List<Categoria> categorias =  (List<Categoria>)categoriaCrudRepository.findAllByOrderByDescripcionAsc();
        return mapper.toCategories(categorias);
    }

    @Override
    public Category save(Category category) {
        Categoria categoria = mapper.toCategoria(category);
        return mapper.toCategory(categoriaCrudRepository.save(categoria));
    }
    @Override
    public Optional<Category> getCategory(int categoryId) {
        return categoriaCrudRepository.findById(categoryId).map(producto -> mapper.toCategory(producto));
    }
}
