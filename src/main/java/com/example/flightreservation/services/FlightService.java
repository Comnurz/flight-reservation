package com.example.flightreservation.services;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.FlightRequest;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.repositories.FlightRepository;
import com.example.flightreservation.repositories.SeatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService implements IFlightService {

    private final FlightRepository flightRepo;
    private final SeatRepository seatRepo;

    public FlightService(
            final FlightRepository flightRepo,
            final SeatRepository seatRepo
    ) {
        this.flightRepo = flightRepo;
        this.seatRepo = seatRepo;
    }

    @Override
    public Flight addFlight(FlightRequest request) {
        Optional<Flight> oldFlight = this.flightRepo.findByFlightNumber(request.getFlightNumber());
        if (oldFlight.isPresent()) {
            return null;
        }
        Flight flight = Flight.of(
                request.getFlightNumber(),
                request.getDepartureDateTime(),
                request.getArrivalDateTime(),
                request.getOriginAirportCode(),
                request.getDestinationAirportCode(),
                request.getAirlineCode(),
                request.getFlightStatus(),
                request.getDescription(),
                request.getPrice()
        );

        this.flightRepo.save(flight);
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= request.getAvailableSeats(); i++) {
            seats.add(Seat.of(
                    flight.getFlightNumber() + i,
                    flight.getFlightId(),
                    "AVAILABLE"
            ));
        }

        this.seatRepo.saveAll(seats);

        return flight;
    }

    @Override
    public List<Flight> getUpcomingFlights() {
        LocalDateTime now = LocalDateTime.now();
        return this.flightRepo.findByDepartureDateTimeAfter(java.sql.Timestamp.valueOf(now));
    }

    @Override
    public List<Flight> getAllFlights() {
        return this.flightRepo.findAll();
    }

    @Override
    public Flight getFlight(String flightNumber) {
        LocalDateTime now = LocalDateTime.now();

        Optional<Flight> flightOptional = this.flightRepo.findByFlightNumberAndDepartureDateTimeAfter(
                flightNumber,
                java.sql.Timestamp.valueOf(now)
            );
        return flightOptional.orElse(null);
    }

    @Override
    @Transactional
    public void deleteFlightByFlightNumber(String flightNumber) throws ResourceNotFoundException {
        boolean exists = this.flightRepo.existsByFlightNumber(flightNumber);
        if (!exists) {
            throw new ResourceNotFoundException("Flight not found with flight number: " + flightNumber);
        }
        this.seatRepo.deleteByFlightNumber(flightNumber);
        this.flightRepo.deleteByFlightNumber(flightNumber);
    }

    @Override
    @Transactional
    public Flight updateFlight(
            String flightNumber,
            FlightRequest request
    ) throws ResourceNotFoundException {
        Flight existingFlight = this.flightRepo.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight not found with flight number: " + flightNumber
                ));

        // Update only non-null fields from the request
        Optional.ofNullable(request.getAirlineCode()).ifPresent(existingFlight::setAirlineCode);
        Optional.ofNullable(request.getDestinationAirportCode()).ifPresent(existingFlight::setDestinationAirportCode);
        Optional.ofNullable(request.getOriginAirportCode()).ifPresent(existingFlight::setOriginAirportCode);
        Optional.ofNullable(request.getFlightStatus()).ifPresent(existingFlight::setFlightStatus);
        Optional.ofNullable(request.getDepartureDateTime()).ifPresent(existingFlight::setDepartureDateTime);
        Optional.ofNullable(request.getArrivalDateTime()).ifPresent(existingFlight::setArrivalDateTime);
        Optional.ofNullable(request.getDescription()).ifPresent(existingFlight::setDescription);
        Optional.ofNullable(request.getPrice()).ifPresent(existingFlight::setPrice);

        int currentSeats = this.seatRepo.countByFlightIdAndStatus(existingFlight.getFlightId(), "AVAILABLE");

        if (Optional.ofNullable(request.getAvailableSeats()).isPresent()) {
            int requestedSeats = request.getAvailableSeats();

            if (requestedSeats > currentSeats) {
                // Add seats if requested seats are more than current seats
                List<Seat> seats = new ArrayList<>();
                for (int i = currentSeats + 1; i <= requestedSeats; i++) {
                    seats.add(Seat.of(
                            flightNumber + i,
                            existingFlight.getFlightId(),
                            "AVAILABLE"
                    ));
                }
                this.seatRepo.saveAll(seats);
            } else if (requestedSeats < currentSeats) {
                // Remove seats if requested seats are less than current seats
                List<Seat> seatsToRemove = this.seatRepo.findTopByFlightIdAndStatusOrderBySeatNumberDesc(
                        existingFlight.getFlightId(),
                        "AVAILABLE",
                        Pageable.ofSize(currentSeats - requestedSeats)
                );
                this.seatRepo.deleteAll(seatsToRemove);
            }
        }
        return this.flightRepo.save(existingFlight);
    }

    @Override
    public List<Seat> getFlightSeatsByStatus(
            String flightNumber,
            String seatStatus
    ) throws ResourceNotFoundException {
        Flight flight = this.flightRepo.findByFlightNumber(flightNumber).orElseThrow(() ->
                new ResourceNotFoundException("Flight not found with flight number: " + flightNumber));
        if (seatStatus.equals("ALL")) {
            return this.seatRepo.findByFlightId(flight.getFlightId());
        }
        return this.seatRepo.findByFlightIdAndStatus(flight.getFlightId(), seatStatus);
    }
}
