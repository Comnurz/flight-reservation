-- Create Airport Table
CREATE TABLE airport (
                         airport_id SERIAL PRIMARY KEY,
                         airport_code VARCHAR(3) UNIQUE,
                         airport_name VARCHAR(100),
                         location VARCHAR(255)
);

-- Create Airline Table
CREATE TABLE airline (
                         airline_id SERIAL PRIMARY KEY,
                         airline_code VARCHAR(3) UNIQUE,
                         airline_name VARCHAR(100)
);

-- Create Flight Table
CREATE TABLE flight (
                        flight_id SERIAL PRIMARY KEY,
                        flight_number VARCHAR(20) UNIQUE,
                        departure_date_time TIMESTAMP,
                        arrival_date_time TIMESTAMP,
                        origin_airport_code VARCHAR(3),
                        destination_airport_code VARCHAR(3),
                        available_seats INT,
                        airline_code VARCHAR(3),
                        flight_class VARCHAR(3),
                        flight_status VARCHAR(20),
                        price DECIMAL(10, 2),
                        FOREIGN KEY (origin_airport_code) REFERENCES airport(airport_code),
                        FOREIGN KEY (destination_airport_code) REFERENCES airport(airport_code),
                        FOREIGN KEY (airline_code) REFERENCES airline(airline_code)
);

-- Create Passenger Table
CREATE TABLE passenger (
                           passenger_id SERIAL PRIMARY KEY,
                           first_name VARCHAR(50),
                           last_name VARCHAR(50),
                           email VARCHAR(100),
                           passport_number VARCHAR(20) UNIQUE
);

-- Create Booking Table
CREATE TABLE booking (
                         booking_id SERIAL PRIMARY KEY,
                         flight_id INT,
                         passenger_id INT,
                         payment_status VARCHAR(20),
                         FOREIGN KEY (flight_id) REFERENCES flight(flight_id),
                         FOREIGN KEY (passenger_id) REFERENCES passenger(passenger_id)
);

-- Create Payment Table
CREATE TABLE payment (
                         payment_id SERIAL PRIMARY KEY,
                         booking_id INT UNIQUE,
                         payment_method VARCHAR(50),
                         amount DECIMAL(10, 2),
                         transaction_date_time TIMESTAMP,
                         FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);
