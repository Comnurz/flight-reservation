CREATE TABLE seat (
                      seat_id SERIAL PRIMARY KEY,
                      seat_number VARCHAR(20) NOT NULL UNIQUE,
                      flight_id INT NOT NULL,
                      passenger_id INT,
                      status VARCHAR(20) DEFAULT 'AVAILABLE', -- e.g., AVAILABLE, RESERVED, BOOKED
                      FOREIGN KEY (flight_id) REFERENCES Flight(flight_id),
                      FOREIGN KEY (passenger_id) REFERENCES Passenger(passenger_id),
                      UNIQUE (seat_number, passenger_id) -- Ensure no duplicate seat numbers for the same flight
);