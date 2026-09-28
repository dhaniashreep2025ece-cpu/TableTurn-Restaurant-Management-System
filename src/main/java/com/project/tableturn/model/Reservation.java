package com.project.tableturn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    private String customerName;
    private String contactNumber;
    private Integer partySize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    public enum ReservationStatus { CONFIRMED, SEATED, COMPLETED, CANCELLED }

    public Reservation() {}

    public Long getId() { return id; }
    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public Integer getPartySize() { return partySize; }
    public void setPartySize(Integer partySize) { this.partySize = partySize; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}