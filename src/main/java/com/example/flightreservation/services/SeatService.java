package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.repositories.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatService implements ISeatService {

    private final SeatRepository seatRepo;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepo = seatRepository;
    }

    public Seat createSeat(
            String seatNumber,
            Integer flightNumber,
            String seatStatus
    ) {
        Seat seat = Seat.of(
                seatNumber,
                flightNumber,
                seatStatus
        );

        return this.seatRepo.save(seat);
    }

    public Seat createSeat(Seat seat) {
        return this.seatRepo.save(seat);
    }
}
