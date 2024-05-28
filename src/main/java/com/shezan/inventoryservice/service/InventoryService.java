package com.shezan.inventoryservice.service;

import com.shezan.inventoryservice.dao.InventoryDao;
import com.shezan.inventoryservice.dao.ItemHistoryDao;
import com.shezan.inventoryservice.model.Item;
import com.shezan.inventoryservice.model.ItemHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryDao inventoryDao;

    public InventoryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    @Autowired
    ItemHistoryDao itemHistoryDao;

    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(inventoryDao.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Item> getItem(int id) {
        Item item = inventoryDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    public ResponseEntity<String> addItem(Item item) {
        inventoryDao.save(item);
        return new ResponseEntity<>("Item Successfully Added", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteItem(int id) {
        inventoryDao.deleteById(id);
        return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> updateItem(Item item) {
        Optional<Item> existingItemCheck = inventoryDao.findById(item.getId());
        if (existingItemCheck.isPresent()) {
            Item existingItem = existingItemCheck.get();
            existingItem.setName(item.getName());
            existingItem.setBorrowedBy(item.getBorrowedBy());
            existingItem.setDescription(item.getDescription());
            inventoryDao.save(existingItem);
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> borrowItem(Integer itemId, Integer customerId) {
        Optional<Item> existingItemCheck = inventoryDao.findById(itemId);

        if (existingItemCheck.isPresent()) {
            Item existingItem = existingItemCheck.get();
            if (existingItem.getBorrowedBy() == null) {
                existingItem.setBorrowedBy(customerId);
                ItemHistory historyEntry = ItemHistory.builder().item(existingItem).customerId(customerId).borrowedAt(LocalDateTime.now()).build();
                itemHistoryDao.save(historyEntry);
                return new ResponseEntity<>("Item Borrowed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Item already Borrowed", HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<String> returnItem(Integer itemId, Integer customerId) {
        Optional<Item> existingItemCheck = inventoryDao.findById(itemId);
        if (existingItemCheck.isPresent()) {
            Item existingItem = existingItemCheck.get();
            if (existingItem.getBorrowedBy() != null && Objects.equals(customerId, existingItem.getBorrowedBy())) {
                existingItem.setBorrowedBy(null);
                inventoryDao.save(existingItem);
                // Update the ItemHistory entity
                Optional<ItemHistory> optionalItemHistory = itemHistoryDao.findByItemIdAndCustomerIdAndReturnedAtIsNull(itemId, customerId);
                if (optionalItemHistory.isPresent()) {
                    ItemHistory itemHistory = optionalItemHistory.get();
                    itemHistory.setReturnedAt(LocalDateTime.now()); // Set return timestamp
                    itemHistoryDao.save(itemHistory);
                    return new ResponseEntity<>("Item Returned successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Item  History not found", HttpStatus.NOT_FOUND);
                }

            } else {
                return new ResponseEntity<>("Item not borrowed or wrong customer", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<ItemHistory>> getItemHistory(Integer itemId) {
        Optional<List<ItemHistory>> existingItemCheck = itemHistoryDao.findByItemId(itemId);
        if (existingItemCheck.isPresent()) {
            List<ItemHistory> itemHistory = existingItemCheck.get();
            return new ResponseEntity<>(itemHistory, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
