package com.supplytracker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="items")

@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    // LOT OF Items CAN HAVE single USER (SUPPLIER)
    @JoinColumn(name="supplier_id", nullable = false)
    private User supplier;

    @Column(nullable = false)
    private LocalDateTime datetime;
}
