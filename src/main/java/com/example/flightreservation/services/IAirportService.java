package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airport;

public interface IAirportService {
    public Airport addAirport(
            String airportCode,
            String airportName,
            String location
    );
}
