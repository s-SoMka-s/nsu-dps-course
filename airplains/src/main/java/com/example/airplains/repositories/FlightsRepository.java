package com.example.airplains.repositories;

import com.example.airplains.entities.flights.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

public interface FlightsRepository extends JpaRepository<Flight, Integer> {
    @Query("select f from Flight f where " +
            "f.departureAirport.airportCode = ?1"
    )
    List<Flight> findAllByDepartureAirportCode(String code);

    @Query("select f from Flight f where " +
            "f.arrivalAirport.airportCode = ?1")
    List<Flight> findAllByArrivalAirportCode(String code);

    @Query("select f from Flight f where " +
            "f.departureAirport.airportCode = ?1 and" +
            "(f.scheduledDeparture between ?2 and ?3)"
    )
    List<Flight> findAllByDepartureAirport(
            String airport,
            OffsetDateTime scheduledDeparture,
            OffsetDateTime scheduledDeparture2
    );

    @Query("select f from Flight f where " +
            "f.departureAirport.city = ?1 and " +
            "(f.scheduledDeparture between ?2 and ?3)")
    List<Flight> findAllByDepartureCity(
            String city,
            OffsetDateTime scheduledDeparture,
            OffsetDateTime scheduledDeparture2
    );
}
