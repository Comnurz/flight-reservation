package com.example.flightreservation.services;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.BookingRequest;
import com.example.flightreservation.models.storage.Booking;

public interface IBookingService {
    public Booking createBooking(BookingRequest request) throws ResourceNotFoundException;
}
