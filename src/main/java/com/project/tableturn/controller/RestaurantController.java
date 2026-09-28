package com.project.tableturn.controller;

import com.project.tableturn.model.*;
import com.project.tableturn.repository.TableRepository;
import com.project.tableturn.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @Autowired
    private TableRepository tableRepo;

    // View all tables
    @GetMapping("/tables")
    public List<RestaurantTable> getAllTables() {
        return tableRepo.findAll();
    }

    // Add a table manually
    @PostMapping("/tables")
    public RestaurantTable addTable(@RequestParam Integer tableNumber, @RequestParam Integer capacity) {
        return tableRepo.save(new RestaurantTable(tableNumber, capacity));
    }

    // 1. Reserve table with overlap detection
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(
            @RequestParam Long tableId,
            @RequestParam String customerName,
            @RequestParam String phone,
            @RequestParam Integer partySize,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            Reservation res = service.makeReservation(tableId, customerName, phone, partySize, startTime, endTime);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Seat table (Must be FREE)
    @PostMapping("/tables/{id}/seat")
    public ResponseEntity<?> seatTable(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.seatTable(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Place order
    @PostMapping("/tables/{id}/orders")
    public ResponseEntity<?> placeOrder(@PathVariable Long id, @RequestBody List<OrderItem> items) {
        try {
            return ResponseEntity.ok(service.placeOrder(id, items));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Generate bill
    @PostMapping("/tables/{id}/bill")
    public ResponseEntity<?> generateBill(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.generateBill(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. Vacate table
    @PostMapping("/tables/{id}/vacate")
    public ResponseEntity<?> vacateTable(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.vacateTable(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}