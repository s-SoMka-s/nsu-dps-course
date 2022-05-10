package com.example.airplains.repositories;

import com.example.airplains.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportsRepository extends JpaRepository<Airport, String> {
    @Query("SELECT distinct city from Airport")
    List<String> findAllCities();

    List<Airport> findAllByCity(String city);

    Airport findFirstByAirportCode(String code);

    Airport findFirstByCity(String city);
}
