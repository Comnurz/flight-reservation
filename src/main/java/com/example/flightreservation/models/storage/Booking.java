package com.example.flightreservation.models.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "flight_id", nullable = false)
    private Integer flightId;

    @Column(name = "passenger_id", nullable = false)
    private Integer passengerId;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static Booking of(
            Integer flightId,
            Integer passengerId,
            String paymentStatus
    ) {
        Booking booking = new Booking();
        booking.setFlightId(flightId);
        booking.setPassengerId(passengerId);
        booking.setPaymentStatus(paymentStatus);
        return booking;
    }
}
