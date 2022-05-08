package com.example.airplains.repositories;

import com.example.airplains.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportsRepository extends JpaRepository<Airport, String> {
    List<Airport> findAllByCity(String city);

    Airport findFirstByAirportCode(String code);

    Airport findFirstByCity(String city);
}
