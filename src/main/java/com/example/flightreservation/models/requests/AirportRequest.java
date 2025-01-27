package com.example.flightreservation.models.requests;

import javax.validation.constraints.NotBlank;

public class AirportRequest {
    @NotBlank(message = "airportCode is mandatory")
    private String airportCode;

    @NotBlank(message = "airportName is mandatory")
    private String airportName;

    @NotBlank(message = "location is mandatory")
    private String location;

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
