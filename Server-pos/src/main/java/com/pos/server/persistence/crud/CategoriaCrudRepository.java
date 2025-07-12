package com.pos.server.persistence.crud;

import com.pos.server.persistence.entity.Categoria;
import com.pos.server.persistence.entity.Compra;
import com.pos.server.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaCrudRepository extends CrudRepository <Categoria, Integer> {
    List<Categoria> findAllByOrderByDescripcionAsc();
}
