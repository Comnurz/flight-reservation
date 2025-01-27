package com.example.flightreservation.services;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.FlightRequest;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Seat;

import java.util.List;

public interface IFlightService {
    public Flight addFlight(FlightRequest request);

    public List<Flight> getUpcomingFlights();

    public List<Flight> getAllFlights();

    public Flight getFlight(String flightNumber);

    public void deleteFlightByFlightNumber(String flightNumber) throws ResourceNotFoundException;

    public Flight updateFlight(String flightNumber, FlightRequest request) throws ResourceNotFoundException;

    public List<Seat> getFlightSeatsByStatus(String flightNumber, String seatStatus)  throws ResourceNotFoundException;
}
