package com.example.airplains.controllers;

import com.example.airplains.controllers.models.mappers.FlightMapper;
import com.example.airplains.controllers.models.output.AirportInboundScheduleDto;
import com.example.airplains.controllers.models.output.AirportOutboundScheduleDto;
import com.example.airplains.entities.Airport;
import com.example.airplains.repositories.AirportsRepository;
import com.example.airplains.repositories.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("airports")
public class AirportsController {

    private final AirportsRepository airports;
    private final FlightsRepository flights;
    private final FlightMapper flightMapper;

    @Autowired
    public AirportsController(AirportsRepository airports, FlightsRepository flights, FlightMapper mapper) {
        this.airports = airports;
        this.flights = flights;
        this.flightMapper = mapper;
    }

    @GetMapping
    public Collection<Airport> getAll(@RequestParam(name = "city", required = false) String city) {
        if (city == null || city.isEmpty()) {
            return this.airports.findAll();
        }

        return this.airports.findAllByCity(city);
    }

    @GetMapping("{airport}/schedule/inbound")
    public List<AirportInboundScheduleDto> getAirportInboundScheduleInbound(@PathVariable String airport) {
        return this.flights.findAllByArrivalAirport(airport)
                .stream()
                .map(flightMapper::mapInboundSchedule)
                .collect(Collectors.toList());
    }

    @GetMapping("{airport}/schedule/outbound")
    public List<AirportOutboundScheduleDto> getAirportOutboundScheduleInbound(@PathVariable String airport) {
        return this.flights.findAllByDepartureAirport(airport)
                .stream()
                .map(flightMapper::mapOutboundSchedule)
                .collect(Collectors.toList());
    }
}
