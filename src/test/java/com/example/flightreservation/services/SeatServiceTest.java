package com.example.flightreservation.services;

import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.repositories.SeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SeatServiceTest {

    @Autowired
    private SeatService seatService;

    @MockBean
    private SeatRepository seatRepository;

    @Test
    public void testCreateSeatWithParameters_Success() {
        // Arrange
        String seatNumber = "A1";
        Integer flightNumber = 123;
        String seatStatus = "Available";

        Seat seat = Seat.of(seatNumber, flightNumber, seatStatus);

        Mockito.when(seatRepository.save(Mockito.any(Seat.class))).thenReturn(seat);

        // Act
        Seat result = seatService.createSeat(seatNumber, flightNumber, seatStatus);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSeatNumber()).isEqualTo(seatNumber);
        assertThat(result.getFlightId()).isEqualTo(flightNumber);
        assertThat(result.getStatus()).isEqualTo(seatStatus);
    }

    @Test
    public void testCreateSeatWithSeatObject_Success() {
        // Arrange
        Seat seat = Seat.of("B2", 456, "Available");

        Mockito.when(seatRepository.save(Mockito.any(Seat.class))).thenReturn(seat);

        // Act
        Seat result = seatService.createSeat(seat);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSeatNumber()).isEqualTo("B2");
        assertThat(result.getFlightId()).isEqualTo(456);
        assertThat(result.getStatus()).isEqualTo("Available");
    }
}