package com.example.airplains.repositories;

import com.example.airplains.entities.Airport;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.FlightPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlightPriceRepository extends JpaRepository<FlightPrice, Long> {
    @Query("select p from FlightPrice p " +
            "where p.arrivalAirport = ?1 and " +
            "p.departureAirport = ?2 and " +
            "p.fareConditions = ?3")
    FlightPrice findPrice(String arrivalAirport, String departureAirport, FareConditions fareCondition);
}
