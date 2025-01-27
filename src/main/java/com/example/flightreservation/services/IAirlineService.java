package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airline;

public interface IAirlineService {
    public Airline addAirline(String airlineCode, String airlineName);
}
