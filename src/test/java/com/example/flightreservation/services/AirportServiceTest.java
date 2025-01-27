package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airport;
import com.example.flightreservation.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AirportServiceTest {

    @Autowired
    private AirportService airportService;

    @MockBean
    private AirportRepository airportRepository;

    /**
     * This test verifies the behavior of the addAirport method
     * when provided with valid input data.
     */
    @Test
    void addAirport_validInput_createsAndReturnsAirport() {
        // Given
        String airportCode = "ABC";
        String airportName = "Test Airport";
        String location = "Test Location";

        Airport mockAirport = Airport.of(airportCode, airportName, location);

        when(airportRepository.save(any(Airport.class))).thenReturn(mockAirport);

        // When
        Airport result = airportService.addAirport(airportCode, airportName, location);

        // Then
        assertNotNull(result);
        assertEquals(airportCode, result.getAirportCode());
        assertEquals(airportName, result.getAirportName());
        assertEquals(location, result.getLocation());

        Mockito.verify(airportRepository).save(any(Airport.class));
    }
}