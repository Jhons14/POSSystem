package com.pos.server.persistence.crud;

import com.pos.server.persistence.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteCrudRepository extends CrudRepository<Cliente, Integer> {
    Cliente findById (int id);
}
