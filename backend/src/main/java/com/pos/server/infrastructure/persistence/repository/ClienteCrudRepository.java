package com.pos.server.infrastructure.persistence.repository;

import com.pos.server.infrastructure.persistence.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteCrudRepository extends CrudRepository<Cliente, Integer> {
    Cliente findById (int id);
    // Métodos para login
    Optional<Cliente> findByUsername(String username);
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
    @Query("SELECT c FROM Cliente c WHERE c.username = :username OR c.correoElectronico = :email")
    Optional<Cliente> findByUsernameOrCorreoElectronico(
            @Param("username") String username,
            @Param("email") String email
    );
}
