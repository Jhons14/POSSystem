package com.pos.server.infrastructure.persistence.repository;

import com.pos.server.domain.model.Customer;
import com.pos.server.domain.repository.CustomerRepository;
import com.pos.server.infrastructure.persistence.repository.ClienteCrudRepository;
import com.pos.server.infrastructure.persistence.entity.Cliente;
import com.pos.server.infrastructure.persistence.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepository implements CustomerRepository {

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    ClienteCrudRepository clienteCrudRepository;

    @Override
    public Customer save (Customer customer){
        Cliente cliente = customerMapper.toCliente(customer);
        return customerMapper.toCustomer(clienteCrudRepository.save(cliente));
    }

    public Optional<Cliente> findByUsernameOrCorreoElectronico(String username, String email) {
        Optional<Cliente> cliente = clienteCrudRepository.findByUsernameOrCorreoElectronico(username, email);
        return cliente;
    }

    // Métodos adicionales para login
    public Optional<Cliente> findByUsername(String username) {
        Optional<Cliente> cliente = clienteCrudRepository.findByUsername(username);


        return cliente;
    }

    public Optional<Cliente> findByCorreoElectronico(String email) {
        return clienteCrudRepository.findByCorreoElectronico(email);
    }

}
