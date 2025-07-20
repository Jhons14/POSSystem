package com.pos.server.domain.service;

import com.pos.server.domain.model.Purchase;
import com.pos.server.domain.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<Purchase> getAll () {
      return  purchaseRepository.getAll();
    }

    public Optional<List<Purchase>> getByClient(Long clientId){
        return purchaseRepository.getByClient(clientId);
    }

    public Purchase save (Purchase purchase) {

        return purchaseRepository.save(purchase);
    }
}
