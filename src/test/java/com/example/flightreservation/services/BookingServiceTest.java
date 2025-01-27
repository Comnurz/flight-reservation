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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepo;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private FlightRepository flightRepo;

    @MockBean
    private PaymentRepository paymentRepo;

    @MockBean
    private SeatRepository seatRepo;

    @Test
    public void testCreateBooking_Success() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL123");
        bookingRequest.setSeatNumber("1A");
        bookingRequest.setPassportNumber("P123");
        bookingRequest.setFirstName("John");
        bookingRequest.setLastName("Doe");
        bookingRequest.setEmail("john.doe@example.com");
        bookingRequest.setPaymentMethod("CREDIT_CARD");

        Passenger passenger = new Passenger();
        passenger.setPassengerId(1);

        Flight flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber("FL123");

        Seat seat = new Seat();
        seat.setSeatId(1);
        seat.setFlightId(flight.getFlightId());
        seat.setSeatNumber("1A");
        seat.setStatus("AVAILABLE");

        Booking booking = Booking.of(flight.getFlightId(), passenger.getPassengerId(), "PENDING");
        booking.setBookingId(1);

        Payment payment = Payment.of(
                booking.getBookingId(),
                bookingRequest.getPaymentMethod(),
                java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
                100.0);

        Mockito.when(passengerService.getOrCreatePassenger("P123", "John", "Doe", "john.doe@example.com"))
                .thenReturn(passenger);
        Mockito.when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));
        Mockito.when(seatRepo.findByFlightIdAndSeatNumberAndStatus(1, "1A", "AVAILABLE"))
                .thenReturn(Optional.of(seat));
        Mockito.when(bookingRepo.save(Mockito.any(Booking.class))).thenReturn(booking);
        Mockito.when(paymentRepo.save(Mockito.any(Payment.class))).thenReturn(payment);

        Booking createdBooking = bookingService.createBooking(bookingRequest);

        assertEquals("PAID", createdBooking.getPaymentStatus());
    }

    @Test
    public void testCreateBooking_FlightNotFound() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL123");
        bookingRequest.setSeatNumber("1A");
        bookingRequest.setPassportNumber("P123");
        bookingRequest.setFirstName("John");
        bookingRequest.setLastName("Doe");
        bookingRequest.setEmail("john.doe@example.com");
        bookingRequest.setPaymentMethod("CREDIT_CARD");

        Mockito.when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });

        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    public void testCreateBooking_SeatNotFound() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL123");
        bookingRequest.setSeatNumber("1A");
        bookingRequest.setPassportNumber("P123");
        bookingRequest.setFirstName("John");
        bookingRequest.setLastName("Doe");
        bookingRequest.setEmail("john.doe@example.com");
        bookingRequest.setPaymentMethod("CREDIT_CARD");

        Passenger passenger = new Passenger();
        passenger.setPassengerId(1);

        Flight flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber("FL123");

        Mockito.when(passengerService.getOrCreatePassenger("P123", "John", "Doe", "john.doe@example.com"))
                .thenReturn(passenger);
        Mockito.when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));
        Mockito.when(seatRepo.findByFlightIdAndSeatNumberAndStatus(1, "1A", "AVAILABLE"))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });

        assertEquals("Seat is not available", exception.getMessage());
    }

    @Test
    public void testCreateBooking_SeatNotAvailable() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFlightNumber("FL123");
        bookingRequest.setSeatNumber("1A");
        bookingRequest.setPassportNumber("P123");
        bookingRequest.setFirstName("John");
        bookingRequest.setLastName("Doe");
        bookingRequest.setEmail("john.doe@example.com");
        bookingRequest.setPaymentMethod("CREDIT_CARD");
        Passenger passenger = new Passenger();
        passenger.setPassengerId(1);

        Flight flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber("FL123");

        Seat seat = new Seat();
        seat.setSeatId(1);
        seat.setFlightId(flight.getFlightId());
        seat.setSeatNumber("1A");
        seat.setStatus("BOOKED");

        Mockito.when(passengerService.getOrCreatePassenger("P123", "John", "Doe", "john.doe@example.com"))
                .thenReturn(passenger);
        Mockito.when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));
        Mockito.when(seatRepo.findByFlightIdAndSeatNumberAndStatus(1, "1A", "BOOKED"))
                .thenReturn(Optional.of(seat));
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });
        assertEquals("Seat is not available", exception.getMessage());
        Mockito.verify(passengerService, Mockito.times(1)).getOrCreatePassenger("P123", "John", "Doe", "john.doe@example.com");
        Mockito.verify(flightRepo, Mockito.times(1)).findByFlightNumber("FL123");
        Mockito.verify(seatRepo, Mockito.times(1)).findByFlightIdAndSeatNumberAndStatus(1, "1A", "AVAILABLE");
        Mockito.verify(bookingRepo, Mockito.never()).save(Mockito.any(Booking.class));
        Mockito.verify(paymentRepo, Mockito.never()).save(Mockito.any(Payment.class));
    }
}