package com.example.airplains.repositories;

import com.example.airplains.entities.flights.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface FlightsRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findAllByDepartureAirport(String airport);

    List<Flight> findAllByArrivalAirport(String airport);

    @Query("select f from Flight f where " +
            "f.departureAirport.airportCode = ?1 and" +
            "(f.scheduledDeparture between ?2 and ?3)"
    )
    List<Flight> findAllByDepartureAirport(
            String airport,
            Date scheduledDeparture,
            Date scheduledDeparture2
    );

    @Query("select f from Flight f where " +
            "f.departureAirport.city = ?1 and " +
            "(f.scheduledDeparture between ?2 and ?3)")
    List<Flight> findAllByDepartureCity(
            String city,
            Date scheduledDeparture,
            Date scheduledDeparture2
    );
}
