package com.example.flightreservation.models.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @Column(name = "seat_number", unique = true, nullable = false)
    private String seatNumber;

    @Column(name = "flight_id", nullable = false)
    private Integer flightId;

    @Column(name = "passenger_id")
    private Integer passengerId;

    @Column(name = "status", nullable = false)
    private String status;

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Seat of(
            String seatNumber,
            Integer flightId,
            String status
    ) {
        Seat seat = new Seat();
        seat.setSeatNumber(seatNumber);
        seat.setFlightId(flightId);
        seat.setStatus(status);
        return seat;
    }
}
