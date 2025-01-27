package com.example.flightreservation.models.storage;

import javax.persistence.*;

@Entity
@Table(name = "airline")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private Long airlineId;

    @Column(name = "airline_code", nullable = false)
    private String airlineCode;

    @Column(name = "airline_name")
    private String airlineName;

    public Long getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Long airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public static Airline of(
            String airlineCode,
            String airlineName
    ) {
        Airline airline = new Airline();
        airline.setAirlineCode(airlineCode);
        airline.setAirlineName(airlineName);
        return airline;
    }
}
