package com.project.tableturn.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer tableNumber;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.FREE;

    public enum TableStatus { FREE, OCCUPIED, RESERVED }

    public RestaurantTable() {}

    public RestaurantTable(Integer tableNumber, Integer capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = TableStatus.FREE;
    }

    public Long getId() { return id; }
    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}