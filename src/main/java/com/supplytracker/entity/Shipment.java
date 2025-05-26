package com.supplytracker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="shipments")
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private String fromLocation;

    @Column(nullable = false)
    private String toLocation;


    @Column(nullable = false)
    private LocalDateTime expectedDelivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus currentStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assigned_transporter_id")
    private User assignedTransporter;




}
