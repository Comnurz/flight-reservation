package com.example.flightreservation.controllers;

import com.example.flightreservation.exceptions.ResourceNotFoundException;
import com.example.flightreservation.models.requests.FlightRequest;
import com.example.flightreservation.models.responses.ListResponse;
import com.example.flightreservation.models.storage.Flight;
import com.example.flightreservation.models.storage.Seat;
import com.example.flightreservation.services.IFlightService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FlightController.class)
public class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFlightService flightService;

    @Test
    public void createFlight_withValidRequest_returnsCreatedFlightResponse() throws Exception {
        Flight mockFlight = new Flight();
        mockFlight.setFlightId(1);
        mockFlight.setFlightNumber("FL123");
        Mockito.when(flightService.addFlight(any(FlightRequest.class))).thenReturn(mockFlight);

        String departureDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
        String arrivalDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600000));

        String flightRequestJson = String.format(
                "{\"flightNumber\":\"FL123\"," +
                        "\"departureDateTime\":\"%s\"," +
                        "\"arrivalDateTime\":\"%s\"," +
                        "\"originAirportCode\":\"AAA\"," +
                        "\"destinationAirportCode\":\"BBB\"," +
                        "\"airlineCode\":\"AB\"," +
                        "\"flightStatus\":\"ON_TIME\"," +
                        "\"availableSeats\":100," +
                        "\"description\":\"Test flight\"}",
                departureDateTime, arrivalDateTime
        );

        mockMvc.perform(post("/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightId").value(1))
                .andExpect(jsonPath("$.flightNumber").value("FL123"));
    }

    @Test
    public void createFlight_withInvalidRequest_returnsBadRequest() throws Exception {
        String flightRequestJson = "{\"flightNumber\":\"\"}";

        mockMvc.perform(post("/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFlight_withNullFlightFromService_returnsBadRequest() throws Exception {
        Mockito.when(flightService.addFlight(any(FlightRequest.class))).thenReturn(null);

        String departureDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
        String arrivalDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600000));

        String flightRequestJson = String.format(
                "{\"flightNumber\":\"FL123\"," +
                        "\"departureDateTime\":\"%s\"," +
                        "\"arrivalDateTime\":\"%s\"," +
                        "\"originAirportCode\":\"AAA\"," +
                        "\"destinationAirportCode\":\"BBB\"," +
                        "\"airlineCode\":\"AB\"," +
                        "\"flightStatus\":\"ON_TIME\"," +
                        "\"availableSeats\":100," +
                        "\"description\":\"Test flight\"}",
                departureDateTime, arrivalDateTime
        );

        mockMvc.perform(post("/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void listUpcomingFlights_withValidResponse_returnsFlightList() throws Exception {

        Date departureDateTime = new Date(); // Current date and time
        Date arrivalDateTime = new Date(System.currentTimeMillis() + 3600000); // One hour later
        List<Flight> mockFlights = List.of(
                Flight.of(
                        "FL123",
                        departureDateTime,
                        arrivalDateTime,
                        "AAA",
                        "BBB",
                        "CCC",
                        "ON_TIME",
                        "DESC",
                        100.0),
                Flight.of(
                        "FL456",
                        departureDateTime,
                        arrivalDateTime,
                        "AAA",
                        "BBB",
                        "CCC",
                        "WAITING",
                        "DESC",
                        100.0)
        );
        ListResponse<Flight> mockResponse = new ListResponse<>();
        mockResponse.setList(mockFlights);
        mockResponse.setStatus(200);
        Mockito.when(flightService.getUpcomingFlights()).thenReturn(mockFlights);

        mockMvc.perform(get("/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].flightNumber").value("FL123"))
                .andExpect(jsonPath("$.list[1].flightNumber").value("FL456"));
    }

    @Test
    public void listUpcomingFlights_withEmptyResponse_returnsEmptyList() throws Exception {
        List<Flight> mockFlights = List.of();
        ListResponse<Flight> mockResponse = new ListResponse<>();
        mockResponse.setList(mockFlights);
        mockResponse.setStatus(200);
        Mockito.when(flightService.getUpcomingFlights()).thenReturn(mockFlights);

        mockMvc.perform(get("/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.list").isEmpty());
    }

    @Test
    public void listAllFlights_withValidResponse_returnsFlightList() throws Exception {
        List<Flight> mockFlights = List.of(
                Flight.of(
                        "FL123",
                        new Date(),
                        new Date(System.currentTimeMillis() + 3600000),
                        "AAA",
                        "BBB",
                        "CCC",
                        "ON_TIME",
                        "DESC",
                        100.0),
                Flight.of(
                        "FL456",
                        new Date(),
                        new Date(System.currentTimeMillis() + 7200000),
                        "DDD",
                        "EEE",
                        "FFF",
                        "DELAYED",
                        "DESC2",
                        100.0)
        );
        ListResponse<Flight> mockResponse = new ListResponse<>();
        mockResponse.setList(mockFlights);
        mockResponse.setStatus(200);
        Mockito.when(flightService.getAllFlights()).thenReturn(mockFlights);

        mockMvc.perform(get("/v1/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].flightNumber").value("FL123"))
                .andExpect(jsonPath("$.list[1].flightNumber").value("FL456"));
    }

    @Test
    public void listAllFlights_withEmptyResponse_returnsEmptyList() throws Exception {
        List<Flight> mockFlights = List.of();
        ListResponse<Flight> mockResponse = new ListResponse<>();
        mockResponse.setList(mockFlights);
        mockResponse.setStatus(200);
        Mockito.when(flightService.getAllFlights()).thenReturn(mockFlights);

        mockMvc.perform(get("/v1/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.list").isEmpty());
    }

    @Test
    public void getFlight_withValidFlightNumber_returnsFlight() throws Exception {
        Flight mockFlight = Flight.of(
                "FL123",
                new Date(),
                new Date(System.currentTimeMillis() + 3600000),
                "AAA",
                "BBB",
                "AB",
                "ON_TIME",
                "Test description",
                100.0
        );
        Mockito.when(flightService.getFlight("FL123")).thenReturn(mockFlight);

        mockMvc.perform(get("/v1/flights/FL123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("FL123"))
                .andExpect(jsonPath("$.originAirportCode").value("AAA"))
                .andExpect(jsonPath("$.destinationAirportCode").value("BBB"));
    }

    @Test
    public void getFlight_withNonExistentFlightNumber_returnsNotFound() throws Exception {
        Mockito.when(flightService.getFlight("FL999")).thenReturn(null);

        mockMvc.perform(get("/v1/flights/FL999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFlight_withValidFlightNumber_returnsNoContent() throws Exception {
        Mockito.doNothing().when(flightService).deleteFlightByFlightNumber("FL123");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/v1/flights/FL123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteFlight_withNonExistentFlightNumber_returnsNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Flight not found")).when(flightService).deleteFlightByFlightNumber("FL999");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/v1/flights/FL999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void updateFlight_withValidRequest_returnsUpdatedFlight() throws Exception {
        Flight mockFlight = Flight.of(
                "FL123",
                new Date(),
                new Date(System.currentTimeMillis() + 3600000),
                "AAA",
                "BBB",
                "AB",
                "ON_TIME",
                "Updated description",
                150.0
        );
        Mockito.when(flightService.updateFlight(Mockito.eq("FL123"), any(FlightRequest.class))).thenReturn(mockFlight);

        String updatedFlightRequestJson = String.format(
                "{\"flightNumber\":\"FL123\"," +
                        "\"departureDateTime\":\"%s\"," +
                        "\"arrivalDateTime\":\"%s\"," +
                        "\"originAirportCode\":\"AAA\"," +
                        "\"destinationAirportCode\":\"BBB\"," +
                        "\"airlineCode\":\"AB\"," +
                        "\"flightStatus\":\"ON_TIME\"," +
                        "\"availableSeats\":50," +
                        "\"description\":\"Updated description\"," +
                        "\"price\":150.0}",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600000))
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/v1/flights/FL123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedFlightRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("FL123"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(150.0));
    }

    @Test
    public void updateFlight_withInvalidRequest_returnsBadRequest() throws Exception {
        String invalidFlightRequestJson = "{\"flightNumber\":\"\"}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/v1/flights/FL123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFlightRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateFlight_withNonExistentFlightNumber_returnsNotFound() throws Exception {
        Mockito.when(flightService.updateFlight(Mockito.eq("FL999"), any(FlightRequest.class)))
                .thenThrow(new ResourceNotFoundException("Flight not found"));

        String flightRequestJson = String.format(
                "{\"flightNumber\":\"FL999\"," +
                        "\"departureDateTime\":\"%s\"," +
                        "\"arrivalDateTime\":\"%s\"," +
                        "\"originAirportCode\":\"AAA\"," +
                        "\"destinationAirportCode\":\"BBB\"," +
                        "\"airlineCode\":\"AB\"," +
                        "\"flightStatus\":\"ON_TIME\"," +
                        "\"availableSeats\":100," +
                        "\"description\":\"Test flight\"}",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date(System.currentTimeMillis() + 3600000))
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .patch("/v1/flights/FL999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightRequestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAvailableSeatsByFlightNumber_withValidFlight_returnsSeats() throws Exception {
        List<Seat> mockSeats = List.of(
                Seat.of("1A", 1, "AVAILABLE"),
                Seat.of("1B", 1, "AVAILABLE")
        );
        Mockito.when(flightService.getFlightSeatsByStatus("FL123", "AVAILABLE")).thenReturn(mockSeats);

        mockMvc.perform(get("/v1/flights/FL123/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].seatNumber").value("1A"))
                .andExpect(jsonPath("$.list[1].seatNumber").value("1B"));
    }

    @Test
    public void getAvailableSeatsByFlightNumber_withNoAvailableSeats_returnsEmptyList() throws Exception {
        List<Seat> mockSeats = List.of();
        Mockito.when(flightService.getFlightSeatsByStatus("FL123", "AVAILABLE")).thenReturn(mockSeats);

        mockMvc.perform(get("/v1/flights/FL123/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isEmpty());
    }

    @Test
    public void getAvailableSeatsByFlightNumber_withNonExistentFlight_returnsNotFound() throws Exception {
        Mockito.when(flightService.getFlightSeatsByStatus("FL999", "AVAILABLE"))
                .thenThrow(new ResourceNotFoundException("Flight not found"));

        mockMvc.perform(get("/v1/flights/FL999/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSeatsByFlightNumberAndStatus_withValidFlightAndStatus_returnsSeats() throws Exception {
        List<Seat> mockSeats = List.of(
                Seat.of("1A", 1, "BOOKED"),
                Seat.of("1B", 1, "BOOKED")
        );
        Mockito.when(flightService.getFlightSeatsByStatus("FL123", "BOOKED")).thenReturn(mockSeats);

        mockMvc.perform(get("/v1/flights/FL123/seats/BOOKED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].seatNumber").value("1A"))
                .andExpect(jsonPath("$.list[1].seatNumber").value("1B"));
    }

    @Test
    public void getSeatsByFlightNumberAndStatus_withNoSeatsOfGivenStatus_returnsEmptyList() throws Exception {
        List<Seat> mockSeats = List.of();
        Mockito.when(flightService.getFlightSeatsByStatus("FL123", "BOOKED")).thenReturn(mockSeats);

        mockMvc.perform(get("/v1/flights/FL123/seats/BOOKED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isEmpty());
    }

    @Test
    public void getSeatsByFlightNumberAndStatus_withNonExistentFlight_returnsNotFound() throws Exception {
        Mockito.when(flightService.getFlightSeatsByStatus("FL999", "BOOKED"))
                .thenThrow(new ResourceNotFoundException("Flight not found"));

        mockMvc.perform(get("/v1/flights/FL999/seats/BOOKED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}