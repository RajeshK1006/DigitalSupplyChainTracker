package com.supplytracker.entity;


// Importing JPA and Lombok
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// This class maps to the Alerts table in the Database
@Entity
@Data
@Table(name="alerts")
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    // Unique ID for each alert entry
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This links each alert to a specific shipment
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="shipment_id" , nullable = false)
    private Shipment shipment;

    // Defines the type of Alert (DAMAGE/DELAY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    // Description about the Alert
    @Column(nullable = false)
    private String message;

    // Provides the Timestamp for when the alert was created
    @Column(nullable = false)
    private LocalDateTime createdOn;

    // Whether the alert is resolved or not
    @Column(nullable = false)
    private boolean resolved;



}
