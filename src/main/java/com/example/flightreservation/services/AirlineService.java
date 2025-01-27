package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airline;
import com.example.flightreservation.repositories.AirlineRepository;
import org.springframework.stereotype.Service;


@Service
public class AirlineService implements IAirlineService {

    private final AirlineRepository airlineRepo;

    public AirlineService(
            final AirlineRepository airlineRepo
    ) {
        this.airlineRepo = airlineRepo;
    }

    @Override
    public Airline addAirline(String airlineCode, String airlineName) {
        Airline airline = Airline.of(airlineCode, airlineName);
        return this.airlineRepo.save(airline);
    }
}
