package com.pos.server.infrastructure.persistence.repository;

import com.pos.server.infrastructure.persistence.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteCrudRepository extends CrudRepository<Cliente, Integer> {
    Cliente findById (int id);
}
