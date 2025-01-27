package com.example.flightreservation.controllers;

import com.example.flightreservation.models.requests.AirportRequest;
import com.example.flightreservation.models.storage.Airport;
import com.example.flightreservation.services.IAirportService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AirportController {

    private final IAirportService airportService;

    public AirportController(IAirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping(
            path = "/v1/airports",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<Airport> createAirline(
            @Valid @RequestBody AirportRequest request
    ) {
        Airport airport = this.airportService.addAirport(
                request.getAirportCode(),
                request.getAirportName(),
                request.getLocation()
        );
        if (airport == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(airport, HttpStatus.CREATED);
    }

}
