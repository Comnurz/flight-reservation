package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Flight;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumber(String flightNumber);

    void deleteByFlightNumber(String flightNumber);

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findAll();

    List<Flight> findByDepartureDateTimeAfter(Date departureDateTime);

    Optional<Flight> findByFlightNumberAndDepartureDateTimeAfter(String flightNumber, Date departureDateTimeAfter);

}

