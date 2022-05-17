DROP TABLE IF EXISTS flight_price;
CREATE TABLE flight_price AS (
    SELECT departure_airport, arrival_airport, fare_conditions, seat_no, avg(amount) amount FROM ticket_flights tf
    JOIN flights fl ON tf.flight_id = fl.flight_id
    JOIN boarding_passes bp on tf.ticket_no = bp.ticket_no and tf.flight_id = bp.flight_id
    GROUP BY departure_airport, arrival_airport, fare_conditions, seat_no
);
