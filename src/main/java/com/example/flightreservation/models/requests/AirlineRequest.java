package com.example.flightreservation.models.requests;

import javax.validation.constraints.NotBlank;

public class AirlineRequest {
    @NotBlank(message = "airlineName is mandatory")
    private String airlineName;

    @NotBlank(message = "airlineCode is mandatory")
    private String airlineCode;

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }
}
