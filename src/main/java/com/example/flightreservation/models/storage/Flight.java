package com.example.flightreservation.models.storage;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(nullable = false, unique = true, name = "flight_number") // Ensure database column exists with this name
    private String flightNumber;

    @Column(nullable = false, name = "departure_date_time")
    private Date departureDateTime;

    @Column(nullable = false, name = "arrival_date_time")
    private Date arrivalDateTime;

    @Column(nullable = false, name = "origin_airport_code")
    private String originAirportCode;

    @Column(nullable = false, name = "destination_airport_code")
    private String destinationAirportCode;

    @Column(nullable = false, name = "airline_code")
    private String airlineCode;

    @Column(nullable = false, name = "flight_status")
    private String flightStatus;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    public static Flight of(
            String flightNumber,
            Date departureDateTime,
            Date arrivalDateTime,
            String originAirportCode,
            String destinationAirportCode,
            String airlineCode,
            String flightStatus,
            String description,
            Double price
    ) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(departureDateTime);
        flight.setArrivalDateTime(arrivalDateTime);
        flight.setOriginAirportCode(originAirportCode);
        flight.setDestinationAirportCode(destinationAirportCode);
        flight.setAirlineCode(airlineCode);
        flight.setFlightStatus(flightStatus);
        flight.setDescription(description);
        flight.setPrice(price);
        return flight;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(String originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
