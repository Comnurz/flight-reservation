package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Airport;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByAirportCode(String airportCode);
}
