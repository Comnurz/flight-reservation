package com.example.flightreservation.controllers;

import com.example.flightreservation.models.requests.AirlineRequest;
import com.example.flightreservation.models.storage.Airline;
import com.example.flightreservation.services.IAirlineService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AirlineController is a REST controller that handles airline-related operations.
 * It provides endpoints to manage airline information and interacts with the
 * underlying airline service layer.
 */
@RestController
public class AirlineController {
    private final IAirlineService airlineService;

    public AirlineController(IAirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping(
            path = "/v1/airlines",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<Airline> createAirline(
            @Valid @RequestBody AirlineRequest request
    ) {
        Airline airline = this.airlineService.addAirline(request.getAirlineCode(), request.getAirlineName());
        if (airline == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(airline, HttpStatus.CREATED);
    }
}
