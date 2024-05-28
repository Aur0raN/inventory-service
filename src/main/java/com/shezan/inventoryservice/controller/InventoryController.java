package com.shezan.inventoryservice.controller;

import com.shezan.inventoryservice.model.Item;
import com.shezan.inventoryservice.model.ItemHistory;
import com.shezan.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping("allItems")
    public ResponseEntity<List<Item>> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("getItem")
    public ResponseEntity<Item> getItem(@RequestParam int id) {
        return inventoryService.getItem(id);
    }

    @PostMapping("create")
    public ResponseEntity<String> createCustomer(@RequestBody Item item) {
        return inventoryService.addItem(item);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteItem(@RequestParam int id) {
        return inventoryService.deleteItem(id);
    }

    @PutMapping("update")
    public ResponseEntity<String> updateItem(@RequestBody Item item) {
        return inventoryService.updateItem(item);
    }

    @PostMapping("borrow")
    public ResponseEntity<String> borrowItem(@RequestParam Integer itemId, @RequestParam Integer customerId) {
        return inventoryService.borrowItem(itemId, customerId);
    }

    @PostMapping("return")
    public ResponseEntity<String> returnItem(@RequestParam Integer itemId, @RequestParam Integer customerId) {
        return inventoryService.returnItem(itemId, customerId);
    }

    @GetMapping("itemHistory")
    public ResponseEntity<List<ItemHistory>> getItemHistory(@RequestParam Integer itemId) {
        return inventoryService.getItemHistory(itemId);
    }



}
