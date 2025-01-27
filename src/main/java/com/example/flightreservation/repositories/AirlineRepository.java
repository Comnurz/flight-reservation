package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Airline;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findByAirlineCode(String airlineCode);
}
