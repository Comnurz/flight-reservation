package com.example.flightreservation.repositories;

import com.example.flightreservation.models.storage.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
