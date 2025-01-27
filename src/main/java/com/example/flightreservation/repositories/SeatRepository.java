package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Seat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByFlightIdAndSeatNumberAndStatus(Integer flightId, String seatNumber, String status);

    List<Seat> findByFlightId(Integer flightId);

    List<Seat> findByFlightIdAndStatus(Integer flightId, String status);

    Integer countByFlightIdAndStatus(Integer flightId, String status);

    @Query("SELECT s FROM Seat s WHERE s.flightId = :flightId and s.status=:status ORDER BY s.seatNumber DESC")
    List<Seat> findTopByFlightIdAndStatusOrderBySeatNumberDesc(Integer flightId, String status, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Seat s WHERE s.flightId IN (" +
            "SELECT f.flightId from Flight f where f.flightNumber = :flightNumber)")
    void deleteByFlightNumber(String flightNumber);
}
