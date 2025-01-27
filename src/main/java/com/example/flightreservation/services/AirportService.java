package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airport;
import com.example.flightreservation.repositories.AirportRepository;
import org.springframework.stereotype.Service;

@Service
public class AirportService implements IAirportService {
    private final AirportRepository airportRepo;

    public AirportService(AirportRepository airportRepo) {
        this.airportRepo = airportRepo;
    }

    @Override
    public Airport addAirport(
            String airportCode,
            String airportName,
            String location
    ) {
        Airport airport = Airport.of(
                airportCode,
                airportName,
                location
        );
        return this.airportRepo.save(airport);
    }
}
