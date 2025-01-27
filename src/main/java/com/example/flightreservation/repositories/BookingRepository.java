package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
