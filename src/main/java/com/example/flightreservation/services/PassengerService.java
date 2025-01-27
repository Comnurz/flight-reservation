package com.example.flightreservation.services;

import com.example.flightreservation.models.requests.PassengerRequest;
import com.example.flightreservation.models.storage.Passenger;
import com.example.flightreservation.repositories.PassengerRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PassengerService implements IPassengerService {
    private final PassengerRepository passengerRepo;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepo = passengerRepository;
    }

    public Passenger addPassenger(PassengerRequest request) {
        Optional<Passenger> existingPassenger = this.passengerRepo.findByPassportNumber(
                request.getPassportNumber()
        );
        if (existingPassenger.isPresent()) {
            return null;
        }

        Passenger passenger = Passenger.of(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassportNumber()
        );
        return this.passengerRepo.save(passenger);
    }

    public Passenger addPassenger(
            String passportNumber,
            String firstName,
            String lastName,
            String email
    ) {
        Optional<Passenger> existingPassenger = this.passengerRepo.findByPassportNumber(
                passportNumber
        );
        if (existingPassenger.isPresent()) {
            return null;
        }

        Passenger passenger = Passenger.of(
                firstName,
                lastName,
                email,
                passportNumber
        );
        return this.passengerRepo.save(passenger);
    }

    public Passenger getPassengerByPassportNumber(String passportNumber) {
        return this.passengerRepo.findByPassportNumber(passportNumber).orElse(null);
    }

    public Passenger getOrCreatePassenger(
            String passportNumber,
            String firstName,
            String lastName,
            String email
    ) {
        return this.passengerRepo.findByPassportNumber(passportNumber)
                .orElseGet(() -> this.passengerRepo.save(
                        Passenger.of(firstName, lastName, email, passportNumber)
                ));
    }
}
