package com.project.tableturn.service;

import com.project.tableturn.model.*;
import com.project.tableturn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private ReservationRepository reservationRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private BillRepository billRepo;

    // Feature 1 & 2: Reserve a table & Prevent double-booking
    @Transactional
    public Reservation makeReservation(Long tableId, String customerName, String phone, int partySize, LocalDateTime startTime, LocalDateTime endTime) {
        RestaurantTable table = tableRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table with ID " + tableId + " not found."));

        if (table.getCapacity() < partySize) {
            throw new IllegalStateException("Table capacity (" + table.getCapacity() + ") is insufficient for party size (" + partySize + ").");
        }

        // Overlap verification rule
        List<Reservation> conflicts = reservationRepo.findOverlappingReservations(tableId, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Table " + table.getTableNumber() + " is already reserved during the requested time window.");
        }

        Reservation res = new Reservation();
        res.setTable(table);
        res.setCustomerName(customerName);
        res.setContactNumber(phone);
        res.setPartySize(partySize);
        res.setStartTime(startTime);
        res.setEndTime(endTime);
        return reservationRepo.save(res);
    }

    // Business Rule: Table must be FREE before seating
    @Transactional
    public RestaurantTable seatTable(Long tableId) {
        RestaurantTable table = tableRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table with ID " + tableId + " not found."));

        if (table.getStatus() != RestaurantTable.TableStatus.FREE) {
            throw new IllegalStateException("Table " + table.getTableNumber() + " cannot be seated. Current status: " + table.getStatus());
        }

        table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
        return tableRepo.save(table);
    }

    // Feature 3: Place order against a seated table with multiple items
    @Transactional
    public CustomerOrder placeOrder(Long tableId, List<OrderItem> items) {
        RestaurantTable table = tableRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table with ID " + tableId + " not found."));

        if (table.getStatus() != RestaurantTable.TableStatus.OCCUPIED) {
            throw new IllegalStateException("Orders can only be placed for seated/occupied tables.");
        }

        CustomerOrder order = new CustomerOrder();
        order.setTable(table);
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        return orderRepo.save(order);
    }

    // Feature 4: Generate final bill summing all order items
    @Transactional
    public Bill generateBill(Long tableId) {
        RestaurantTable table = tableRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table with ID " + tableId + " not found."));

        List<CustomerOrder> activeOrders = orderRepo.findByTableIdAndStatus(tableId, CustomerOrder.OrderStatus.ACTIVE);
        if (activeOrders.isEmpty()) {
            throw new IllegalStateException("No active orders found for Table " + table.getTableNumber() + " to generate bill.");
        }

        double total = 0.0;
        for (CustomerOrder order : activeOrders) {
            for (OrderItem item : order.getItems()) {
                total += (item.getPrice() * item.getQuantity());
            }
            order.setStatus(CustomerOrder.OrderStatus.BILLED);
            orderRepo.save(order);
        }

        Bill bill = new Bill(table, total);
        return billRepo.save(bill);
    }

    // Feature 5: Mark table free once billed and vacated
    @Transactional
    public RestaurantTable vacateTable(Long tableId) {
        RestaurantTable table = tableRepo.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table with ID " + tableId + " not found."));

        table.setStatus(RestaurantTable.TableStatus.FREE);
        return tableRepo.save(table);
    }
}