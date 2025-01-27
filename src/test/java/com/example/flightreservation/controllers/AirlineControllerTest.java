package com.example.flightreservation.controllers;

import com.example.flightreservation.models.storage.Airline;
import com.example.flightreservation.services.IAirlineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirlineController.class)
public class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirlineService airlineService;

    @Test
    public void testCreateAirline_Success() throws Exception {
        Airline airline = new Airline();
        airline.setAirlineCode("ABC123");
        airline.setAirlineName("Test Airline");

        Mockito.when(airlineService.addAirline(anyString(), anyString())).thenReturn(airline);

        String requestBody = "{\"airlineName\":\"Test Airline\"," +
                "\"airlineCode\":\"ABC123\"}";

        mockMvc.perform(post("/v1/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.airlineCode").value("ABC123"))
                .andExpect(jsonPath("$.airlineName").value("Test Airline"));
    }

    @Test
    public void testCreateAirline_BadRequest_InvalidRequest() throws Exception {
        String requestBody = "{\"airlineName\":\"Test Airline\"}";

        mockMvc.perform(post("/v1/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}