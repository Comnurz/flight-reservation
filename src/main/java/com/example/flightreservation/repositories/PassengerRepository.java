package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Passenger;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByPassportNumber(String passportNumber);
}
