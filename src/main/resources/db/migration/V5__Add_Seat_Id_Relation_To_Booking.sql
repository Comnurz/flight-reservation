

ALTER TABLE
    "flightReservation".public.booking
ADD COLUMN
    seat_id INT
REFERENCES
    "flightReservation".public.seat(seat_id)