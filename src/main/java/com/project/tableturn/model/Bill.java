package com.project.tableturn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    private Double totalAmount;
    private LocalDateTime billedAt = LocalDateTime.now();
    private Boolean isPaid = false;

    public Bill() {}

    public Bill(RestaurantTable table, Double totalAmount) {
        this.table = table;
        this.totalAmount = totalAmount;
        this.billedAt = LocalDateTime.now();
        this.isPaid = false;
    }

    public Long getId() { return id; }
    public RestaurantTable getTable() { return table; }
    public Double getTotalAmount() { return totalAmount; }
    public LocalDateTime getBilledAt() { return billedAt; }
    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}