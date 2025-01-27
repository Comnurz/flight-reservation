package com.example.flightreservation.controllers;

import com.example.flightreservation.models.storage.Airport;
import com.example.flightreservation.services.IAirportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportController.class)
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAirportService airportService;

    @Test
    public void testCreateAirline_Success() throws Exception {
        Airport mockAirport = new Airport();
        mockAirport.setAirportCode("ABC123");
        mockAirport.setAirportName("Test Airport");
        mockAirport.setLocation("Test Location");

        Mockito.when(airportService.addAirport(anyString(), anyString(), anyString()))
                .thenReturn(mockAirport);

        String requestBody = "{\"airportCode\":\"ABC123\"," +
                "\"airportName\":\"Test Airport\"," +
                "\"location\":\"Test Location\"}";


        mockMvc.perform(post("/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.airportCode").value("ABC123"))
                .andExpect(jsonPath("$.airportName").value("Test Airport"))
                .andExpect(jsonPath("$.location").value("Test Location"));
    }

    @Test
    public void testCreateAirline_BadRequest_MissingField() throws Exception {
        String requestBody = "{\"airportCode\":\"ABC123\"," +
                "\"airportName\":\"Test Airport\"}";

        mockMvc.perform(post("/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAirline_BadRequest_NullResponse() throws Exception {
        Mockito.when(airportService.addAirport(anyString(), anyString(), anyString()))
                .thenReturn(null);

        String requestBody = "{\"airportCode\":\"ABC123\"," +
                "\"airportName\":\"Test Airport\"," +
                "\"location\":\"Test Location\"}";

        mockMvc.perform(post("/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}