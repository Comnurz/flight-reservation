package com.example.flightreservation.controllers;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.FlightRequest;
import com.example.flightreservation.models.responses.ListResponse;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.services.IFlightService;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FlightController {
    private final IFlightService flightService;

    public FlightController(final IFlightService flightSrv) {
        this.flightService = flightSrv;
    }

    @PostMapping(
            path = "/v1/flights",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody FlightRequest request) {
        Flight newFlight = this.flightService.addFlight(request);

        if (newFlight == null) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/v1/flights/{flightNumber}")
                .buildAndExpand(newFlight.getFlightNumber())
                .toUri();

        return ResponseEntity.created(location).body(newFlight);
    }

    @GetMapping(path = "/v1/flights", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<ListResponse<Flight>> listUpcomingFlights() {
        final ListResponse<Flight> response = new ListResponse<>();
        List<Flight> flightList = this.flightService.getUpcomingFlights();
        response.setList(flightList);
        response.setStatus(200);

        return ResponseEntity.ok(response);

    }

    // List all flights
    @GetMapping(path = "/v1/flights/all", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<ListResponse<Flight>> listAllFlights() {
        final ListResponse<Flight> response = new ListResponse<>();
        List<Flight> flightList = this.flightService.getAllFlights();
        response.setList(flightList);
        response.setStatus(200);
        return ResponseEntity.ok(response);

    }

    // Get flight by flight number
    @GetMapping(path = "/v1/flights/{flightNumber}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Flight> getFlight(@PathVariable("flightNumber") String flightNumber) {
        final Flight flight = this.flightService.getFlight(flightNumber);
        if (flight == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flight);
    }

    //delete flight by flight number
    @DeleteMapping(path = "/v1/flights/{flightNumber}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Flight> deleteFlight(@PathVariable("flightNumber") String flightNumber) {
        try {
            this.flightService.deleteFlightByFlightNumber(flightNumber);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(
            path = "/v1/flights/{flightNumber}",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<Flight> updateFlightByFlightNumber(
            @PathVariable String flightNumber,
            @RequestBody FlightRequest request
    ) {
        Flight updatedFlight = null;
        try {
            updatedFlight = this.flightService.updateFlight(flightNumber, request);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        if (updatedFlight == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedFlight);
    }

    @GetMapping(path = "/v1/flights/{flightNumber}/seats", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<ListResponse<Seat>> getAvailableSeatsByFlightNumber(@PathVariable String flightNumber) {
        ListResponse<Seat> response = new ListResponse<>();
        List<Seat> seats;
        try {
            seats = this.flightService.getFlightSeatsByStatus(flightNumber, "AVAILABLE");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        response.setList(seats);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/v1/flights/{flightNumber}/seats/{seatStatus}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<ListResponse<Seat>> getSeatsByFlightNumberAndStatus(
            @PathVariable String flightNumber,
            @PathVariable String seatStatus
    ) {
        ListResponse<Seat> response = new ListResponse<>();
        List<Seat> seats;

        try {
            seats = this.flightService.getFlightSeatsByStatus(flightNumber, seatStatus.toUpperCase());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        response.setList(seats);
        return ResponseEntity.ok(response);
    }
}
