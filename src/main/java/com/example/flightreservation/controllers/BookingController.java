package com.example.flightreservation.controllers;

import com.example.flightreservation.models.requests.BookingRequest;
import com.example.flightreservation.models.responses.BookingResponse;
import com.example.flightreservation.models.storage.Booking;
import com.example.flightreservation.services.IBookingService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(
            path = "/v1/bookings",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        Booking newBooking = this.bookingService.createBooking(request);
        BookingResponse response = new BookingResponse();
        if (newBooking == null) {
            return ResponseEntity.badRequest().build();
        }

        response.setBookingId(newBooking.getBookingId());
        response.setFlightId(newBooking.getFlightId());
        response.setPaymentStatus(newBooking.getPaymentStatus());
        response.setPassengerId(newBooking.getPassengerId());
        response.setStatusCode(201);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
