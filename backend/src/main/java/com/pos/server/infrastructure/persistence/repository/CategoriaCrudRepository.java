package com.pos.server.infrastructure.persistence.repository;

import com.pos.server.infrastructure.persistence.entity.Categoria;
import com.pos.server.infrastructure.persistence.entity.Compra;
import com.pos.server.infrastructure.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaCrudRepository extends CrudRepository <Categoria, Integer> {
    List<Categoria> findAllByOrderByDescripcionAsc();
}
