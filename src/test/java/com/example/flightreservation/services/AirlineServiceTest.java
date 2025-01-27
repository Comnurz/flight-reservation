package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Airline;
import com.example.flightreservation.repositories.AirlineRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class AirlineServiceTest {

    @Autowired
    private AirlineService airlineService;

    @MockBean
    private AirlineRepository airlineRepository;

    @Test
    void testAddAirline_SuccessfullySavesAirline() {
        // Arrange
        String airlineCode = "UA";
        String airlineName = "United Airlines";
        Airline mockAirline = Airline.of(airlineCode, airlineName);

        Mockito.when(airlineRepository.save(any(Airline.class))).thenReturn(mockAirline);

        // Act
        Airline savedAirline = airlineService.addAirline(airlineCode, airlineName);

        // Assert
        assertNotNull(savedAirline);
        assertEquals(airlineCode, savedAirline.getAirlineCode());
        assertEquals(airlineName, savedAirline.getAirlineName());
        Mockito.verify(airlineRepository, Mockito.times(1)).save(any(Airline.class));
    }

    @Test
    void testAddAirline_NullName() {
        // Arrange
        String airlineCode = "DL";
        String airlineName = null;
        Airline mockAirline = Airline.of(airlineCode, airlineName);

        Mockito.when(airlineRepository.save(any(Airline.class))).thenReturn(mockAirline);

        // Act
        Airline savedAirline = airlineService.addAirline(airlineCode, airlineName);

        // Assert
        assertNotNull(savedAirline);
        assertEquals(airlineCode, savedAirline.getAirlineCode());
        assertEquals(airlineName, savedAirline.getAirlineName());
        Mockito.verify(airlineRepository, Mockito.times(1)).save(any(Airline.class));
    }

    @Test
    void testAddAirline_NullCode() {
        // Arrange
        String airlineCode = null;
        String airlineName = "Delta Airlines";
        Airline mockAirline = Airline.of(airlineCode, airlineName);

        Mockito.when(airlineRepository.save(any(Airline.class))).thenReturn(mockAirline);

        // Act
        Airline savedAirline = airlineService.addAirline(airlineCode, airlineName);

        // Assert
        assertNotNull(savedAirline);
        assertEquals(airlineCode, savedAirline.getAirlineCode());
        assertEquals(airlineName, savedAirline.getAirlineName());
        Mockito.verify(airlineRepository, Mockito.times(1)).save(any(Airline.class));
    }

    @Test
    void testAddAirline_EmptyCode() {
        // Arrange
        String airlineCode = "";
        String airlineName = "Southwest Airlines";
        Airline mockAirline = Airline.of(airlineCode, airlineName);

        Mockito.when(airlineRepository.save(any(Airline.class))).thenReturn(mockAirline);

        // Act
        Airline savedAirline = airlineService.addAirline(airlineCode, airlineName);

        // Assert
        assertNotNull(savedAirline);
        assertEquals(airlineCode, savedAirline.getAirlineCode());
        assertEquals(airlineName, savedAirline.getAirlineName());
        Mockito.verify(airlineRepository, Mockito.times(1)).save(any(Airline.class));
    }
}