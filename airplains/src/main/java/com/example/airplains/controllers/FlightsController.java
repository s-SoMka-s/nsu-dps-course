package com.example.airplains.controllers;

import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.servicies.FlightsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flights")
public class FlightsController {

    private final FlightsService flightsService;

    public FlightsController(FlightsService flightsService){
        this.flightsService = flightsService;
    }

    @GetMapping
    public List<Flight> getAll() {
        return flightsService.getAllFlights();
    }

    @GetMapping("route")
    public RouteDto getAllRoutes(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String departureDate,
            @RequestParam("fareCondition") FareConditions fareCondition,
            @RequestParam(required = false, defaultValue = "0") int connections) {
        return flightsService.getRoutes(from, to, departureDate, fareCondition, connections);
    }
}
