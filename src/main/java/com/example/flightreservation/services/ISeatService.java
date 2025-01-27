package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Seat;

public interface ISeatService {
    public Seat createSeat(
            String seatNumber,
            Integer flightNumber,
            String seatStatus
    );

    public Seat createSeat(Seat seat);
}
