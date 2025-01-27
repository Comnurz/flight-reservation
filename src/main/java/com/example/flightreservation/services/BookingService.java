package com.example.flightreservation.services;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.BookingRequest;
import com.example.flightreservation.models.storage.Booking;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Passenger;
import com.example.flightreservation.models.storage.Payment;
import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.repositories.BookingRepository;
import com.example.flightreservation.repositories.FlightRepository;
import com.example.flightreservation.repositories.PaymentRepository;
import com.example.flightreservation.repositories.SeatRepository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepo;
    private final PassengerService passengerService;
    private final FlightRepository flightRepo;
    private final PaymentRepository paymentRepo;
    private final SeatRepository seatRepo;

    public BookingService(
            BookingRepository bookingRepository,
            PassengerService passengerService,
            FlightRepository flightRepository,
            PaymentRepository paymentRepository,
            SeatRepository seatRepository
    ) {
        this.bookingRepo = bookingRepository;
        this.passengerService = passengerService;
        this.flightRepo = flightRepository;
        this.paymentRepo = paymentRepository;
        this.seatRepo = seatRepository;
    }

    /**
     * Creates a new booking for a passenger on a specified flight with the given seat
     * and processes the associated payment. The method validates the availability of the
     * requested flight and seat, and updates the booking and payment status accordingly.
     *
     * @param request the booking request containing flight details, passenger information,
     *                seat number, and payment method
     * @return the completed booking instance with updated payment status
     * @throws ResourceNotFoundException if the flight is not found or the seat is not available
     */
    public Booking createBooking(
            BookingRequest request
    ) throws ResourceNotFoundException {
        Passenger passenger = this.passengerService.getOrCreatePassenger(
                request.getPassportNumber(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
        Flight flight = this.flightRepo.findByFlightNumber(request.getFlightNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        Seat seat = this.seatRepo.findByFlightIdAndSeatNumberAndStatus(
                flight.getFlightId(),
                request.getSeatNumber(),
                "AVAILABLE"
        ).orElseThrow(() -> new ResourceNotFoundException("Seat is not available"));

        Booking booking = Booking.of(
                flight.getFlightId(),
                passenger.getPassengerId(),
                "PENDING"
        );
        Booking booked = this.bookingRepo.save(booking);
        LocalDateTime now = LocalDateTime.now();
        Payment payment = Payment.of(
                booked.getBookingId(),
                request.getPaymentMethod(),
                java.sql.Timestamp.valueOf(now),
                flight.getPrice()
        );
        this.paymentRepo.save(payment);

        seat.setStatus("BOOKED");
        this.seatRepo.save(seat);

        booked.setPaymentStatus("PAID");
        return this.bookingRepo.save(booked);
    }
}
