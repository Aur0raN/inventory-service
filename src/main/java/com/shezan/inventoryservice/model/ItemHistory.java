package com.shezan.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Primary key

    @ManyToOne // Each ItemHistory record belongs to one Item
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;  // Reference to the borrowed item

    private Integer customerId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

}
