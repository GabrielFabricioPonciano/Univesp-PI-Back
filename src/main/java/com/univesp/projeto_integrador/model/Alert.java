package com.univesp.projeto_integrador.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;


    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    @Column(name = "alert_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime alertDate;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus = AlertStatus.ACTIVE;

    @Lob
    private String message;

    // Getters e setters
}

enum AlertType {
    EXPIRATION, LOW_STOCK
}

enum AlertStatus {
    ACTIVE, RESOLVED
}
