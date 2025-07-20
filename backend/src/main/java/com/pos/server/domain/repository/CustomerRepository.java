package com.pos.server.domain.repository;

import com.pos.server.domain.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);
}
