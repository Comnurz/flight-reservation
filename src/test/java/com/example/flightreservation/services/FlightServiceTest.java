package com.example.flightreservation.services;

import com.example.flightreservation.models.requests.FlightRequest;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.repositories.FlightRepository;
import com.example.flightreservation.repositories.SeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FlightServiceTest {

    @Autowired
    private FlightService flightService;

    @MockBean
    private FlightRepository flightRepo;

    @MockBean
    private SeatRepository seatRepo;

    @Test
    void testAddFlightSuccessful() {
        String flightNumber = "FL123";
        FlightRequest request = new FlightRequest();
        request.setFlightNumber(flightNumber);
        request.setDepartureDateTime(new Date());
        request.setArrivalDateTime(new Date(System.currentTimeMillis() + 3600000));
        request.setOriginAirportCode("JFK");
        request.setDestinationAirportCode("LAX");
        request.setAirlineCode("AA");
        request.setFlightStatus("ON_TIME");
        request.setAvailableSeats(5);
        request.setDescription("Test flight");

        when(flightRepo.findByFlightNumber(flightNumber)).thenReturn(Optional.empty());
        when(flightRepo.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Flight result = flightService.addFlight(request);

        assertNotNull(result);
        verify(flightRepo, times(1)).save(any(Flight.class));
        // Capture the argument passed to saveAll
        ArgumentCaptor<Iterable<Seat>> captor = ArgumentCaptor.forClass(Iterable.class);
        verify(seatRepo, times(1)).saveAll(captor.capture());

        // Assert the size of the captured argument
        List<Seat> capturedSeats = (List<Seat>) captor.getValue();
        assertEquals(5, capturedSeats.size(), "Expected 5 seats to be saved");
    }

    @Test
    void testAddFlightAlreadyExists() {
        String flightNumber = "FL123";
        FlightRequest request = new FlightRequest();
        request.setFlightNumber(flightNumber);

        Flight existingFlight = new Flight();
        existingFlight.setFlightNumber(flightNumber);

        when(flightRepo.findByFlightNumber(flightNumber)).thenReturn(Optional.of(existingFlight));

        Flight result = flightService.addFlight(request);

        assertNull(result);
        verify(flightRepo, never()).save(any(Flight.class));
        verify(seatRepo, never()).save(any(Seat.class));
    }

    @Test
    void testAddFlightWithZeroSeats() {
        String flightNumber = "FL123";
        FlightRequest request = new FlightRequest();
        request.setFlightNumber(flightNumber);
        request.setDepartureDateTime(new Date());
        request.setArrivalDateTime(new Date(System.currentTimeMillis() + 3600000));
        request.setOriginAirportCode("JFK");
        request.setDestinationAirportCode("LAX");
        request.setAirlineCode("AA");
        request.setFlightStatus("ON_TIME");
        request.setAvailableSeats(0);
        request.setDescription("Test flight");

        when(flightRepo.findByFlightNumber(flightNumber)).thenReturn(Optional.empty());
        when(flightRepo.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Flight result = flightService.addFlight(request);

        assertNotNull(result);
        verify(flightRepo, times(1)).save(any(Flight.class));
        verify(seatRepo, never()).save(any(Seat.class));
    }
}