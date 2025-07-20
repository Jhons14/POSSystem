package com.pos.server.domain.repository;

import com.pos.server.domain.model.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    List<Purchase> getAll();
    Optional<List<Purchase>> getByClient(Long clientId);
    Purchase save (Purchase purchase);
}
