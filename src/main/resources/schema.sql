-- Create Airport Table
CREATE TABLE airport (
                         airport_code VARCHAR(3) PRIMARY KEY,
                         airport_name VARCHAR(100),
                         location VARCHAR(255)
);

-- Create Airline Table
CREATE TABLE airline (
                         airline_code VARCHAR(3) PRIMARY KEY,
                         airline_name VARCHAR(100)
);

-- Create Flight Table
CREATE TABLE flight (
                        flight_id INT PRIMARY KEY,
                        flight_number VARCHAR(20) UNIQUE,
                        departure_date_time TIMESTAMP,
                        arrival_date_time TIMESTAMP,
                        origin_airport_code VARCHAR(3),
                        destination_airport_code VARCHAR(3),
                        airline_code VARCHAR(3),
                        flight_status VARCHAR(20),
                        price DECIMAL(10, 2),
                        description TEXT,
                        FOREIGN KEY (origin_airport_code) REFERENCES airport(airport_code),
                        FOREIGN KEY (destination_airport_code) REFERENCES airport(airport_code),
                        FOREIGN KEY (airline_code) REFERENCES airline(airline_code)
);

-- Create Passenger Table
CREATE TABLE passenger (
                           passenger_id INT PRIMARY KEY,
                           first_name VARCHAR(50),
                           last_name VARCHAR(50),
                           email VARCHAR(100),
                           passport_number VARCHAR(20) UNIQUE
);

-- Create Booking Table
CREATE TABLE booking (
                         booking_id INT PRIMARY KEY,
                         flight_id INT,
                         passenger_id INT,
                         seat_id INT,
                         payment_status VARCHAR(20),
                         FOREIGN KEY (flight_id) REFERENCES flight(flight_id),
                         FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id)
);

-- Create Payment Table
CREATE TABLE payment (
                         payment_id INT PRIMARY KEY,
                         booking_id INT UNIQUE,
                         payment_method VARCHAR(50),
                         amount DECIMAL(10, 2),
                         transaction_date_time TIMESTAMP,
                         FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);

-- Create Seat Table
CREATE TABLE seat (
                      seat_id SERIAL PRIMARY KEY,
                      seat_number VARCHAR(20) NOT NULL UNIQUE,
                      flight_id INT NOT NULL,
                      passenger_id INT,
                      status VARCHAR(20) DEFAULT 'AVAILABLE',
                      FOREIGN KEY (flight_id) REFERENCES flight(flight_id),
                      FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id),
                      FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
                      UNIQUE (seat_number, passenger_id) -- Ensure no duplicate seat numbers for the same flight
);
