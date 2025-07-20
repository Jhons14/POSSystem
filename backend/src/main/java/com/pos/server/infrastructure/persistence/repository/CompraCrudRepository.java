package com.pos.server.infrastructure.persistence.repository;

import com.pos.server.infrastructure.persistence.entity.Compra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompraCrudRepository extends CrudRepository <Compra, Integer> {
        Optional<List<Compra>> findByIdCliente(Long idCliente);
}
