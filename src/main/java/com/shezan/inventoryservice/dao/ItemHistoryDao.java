package com.shezan.inventoryservice.dao;

import com.shezan.inventoryservice.model.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemHistoryDao extends JpaRepository<ItemHistory, Integer> {
    Optional<ItemHistory> findByItemIdAndCustomerIdAndReturnedAtIsNull(Integer itemId, Integer customerId);
    Optional<List<ItemHistory>> findByItemId(Integer itemId);
}
