package com.project.tableturn.repository;

import com.project.tableturn.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.table.id = :tableId AND r.status = 'CONFIRMED' " +
            "AND r.startTime < :newEnd AND r.endTime > :newStart")
    List<Reservation> findOverlappingReservations(@Param("tableId") Long tableId,
                                                  @Param("newStart") LocalDateTime newStart,
                                                  @Param("newEnd") LocalDateTime newEnd);
}