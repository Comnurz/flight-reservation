package com.example.flightreservation.controllers;

import com.example.flightreservation.models.requests.BookingRequest;
import com.example.flightreservation.models.storage.Booking;
import com.example.flightreservation.services.IBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookingService bookingService;

    @Test
    void shouldCreateBookingAndReturn201() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setFlightId(100);
        booking.setPassengerId(200);
        booking.setPaymentStatus("CONFIRMED");

        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(booking);

        String requestBody = "{ \"flightNumber\": \"FN123\", \"firstName\": \"John\", " +
                "\"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", " +
                "\"passportNumber\": \"A12345678\", \"seatNumber\": \"12A\", " +
                "\"paymentMethod\": \"CREDIT_CARD\" }";

        mockMvc.perform(post("/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.flightId").value(100))
                .andExpect(jsonPath("$.passengerId").value(200))
                .andExpect(jsonPath("$.paymentStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        String requestBody = "{ \"flightNumber\": \"\", \"firstName\": \"\", " +
                "\"lastName\": \"\", \"email\": \"\", \"passportNumber\": \"\", " +
                "\"seatNumber\": \"\", \"paymentMethod\": \"\" }";

        mockMvc.perform(post("/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenBookingServiceReturnsNull() throws Exception {
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(null);

        String requestBody = "{ \"flightNumber\": \"FN123\", \"firstName\": \"Jane\", " +
                "\"lastName\": \"Doe\", \"email\": \"jane.doe@example.com\", " +
                "\"passportNumber\": \"B98765432\", \"seatNumber\": \"14C\", " +
                "\"paymentMethod\": \"CREDIT_CARD\" }";

        mockMvc.perform(post("/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}