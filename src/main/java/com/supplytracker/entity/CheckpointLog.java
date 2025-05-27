package com.supplytracker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.Builder;
@Entity
@Data
@Builder
@Table(name="checkingpoint_logs")
@AllArgsConstructor
@NoArgsConstructor
public class CheckpointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shipment_id", nullable = false)
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private String location;





}
