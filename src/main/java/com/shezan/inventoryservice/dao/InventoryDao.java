package com.shezan.inventoryservice.dao;

import com.shezan.inventoryservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryDao extends JpaRepository<Item, Integer> {
}
