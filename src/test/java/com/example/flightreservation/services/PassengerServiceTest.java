package com.example.flightreservation.services;

import com.example.flightreservation.models.requests.PassengerRequest;
import com.example.flightreservation.models.storage.Passenger;
import com.example.flightreservation.repositories.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PassengerServiceTest {

    @Autowired
    private PassengerService passengerService;

    @MockBean
    private PassengerRepository passengerRepository;

    @Test
    void addPassenger_ShouldAddNewPassenger_WhenPassengerDoesNotExist() {
        PassengerRequest request = new PassengerRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassportNumber("12345678");

        when(passengerRepository.findByPassportNumber("12345678"))
                .thenReturn(Optional.empty());

        Passenger newPassenger = new Passenger();
        newPassenger.setFirstName("John");
        newPassenger.setLastName("Doe");
        newPassenger.setEmail("john.doe@example.com");
        newPassenger.setPassportNumber("12345678");

        when(passengerRepository.save(Mockito.any(Passenger.class)))
                .thenReturn(newPassenger);

        Passenger result = passengerService.addPassenger(request);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("12345678", result.getPassportNumber());

        verify(passengerRepository, times(1)).findByPassportNumber("12345678");
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void addPassenger_ShouldReturnNull_WhenPassengerAlreadyExists() {
        PassengerRequest request = new PassengerRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setEmail("jane.smith@example.com");
        request.setPassportNumber("87654321");

        Passenger existingPassenger = new Passenger();
        existingPassenger.setFirstName("Jane");
        existingPassenger.setLastName("Smith");
        existingPassenger.setEmail("jane.smith@example.com");
        existingPassenger.setPassportNumber("87654321");

        when(passengerRepository.findByPassportNumber("87654321"))
                .thenReturn(Optional.of(existingPassenger));

        Passenger result = passengerService.addPassenger(request);

        assertNull(result);

        verify(passengerRepository, times(1)).findByPassportNumber("87654321");
        verify(passengerRepository, times(0)).save(any(Passenger.class));
    }

    @Test
    void getOrCreatePassenger_ShouldReturnExistingPassenger_WhenPassengerExists() {
        String passportNumber = "11111111";
        String firstName = "Alice";
        String lastName = "Brown";
        String email = "alice.brown@example.com";

        Passenger existingPassenger = new Passenger();
        existingPassenger.setFirstName(firstName);
        existingPassenger.setLastName(lastName);
        existingPassenger.setEmail(email);
        existingPassenger.setPassportNumber(passportNumber);

        when(passengerRepository.findByPassportNumber(passportNumber))
                .thenReturn(Optional.of(existingPassenger));

        Passenger result = passengerService.getOrCreatePassenger(passportNumber, firstName, lastName, email);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(passportNumber, result.getPassportNumber());

        verify(passengerRepository, times(1)).findByPassportNumber(passportNumber);
        verify(passengerRepository, times(0)).save(any(Passenger.class));
    }

    @Test
    void getOrCreatePassenger_ShouldCreateAndReturnNewPassenger_WhenPassengerDoesNotExist() {
        String passportNumber = "22222222";
        String firstName = "Bob";
        String lastName = "Williams";
        String email = "bob.williams@example.com";

        when(passengerRepository.findByPassportNumber(passportNumber))
                .thenReturn(Optional.empty());

        Passenger newPassenger = new Passenger();
        newPassenger.setFirstName(firstName);
        newPassenger.setLastName(lastName);
        newPassenger.setEmail(email);
        newPassenger.setPassportNumber(passportNumber);

        when(passengerRepository.save(any(Passenger.class)))
                .thenReturn(newPassenger);

        Passenger result = passengerService.getOrCreatePassenger(passportNumber, firstName, lastName, email);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(passportNumber, result.getPassportNumber());

        verify(passengerRepository, times(1)).findByPassportNumber(passportNumber);
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }
}