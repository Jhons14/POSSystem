package com.pos.server.persistence;

import com.pos.server.domain.Customer;
import com.pos.server.domain.repository.CustomerRepository;
import com.pos.server.persistence.crud.ClienteCrudRepository;
import com.pos.server.persistence.entity.Cliente;
import com.pos.server.persistence.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
