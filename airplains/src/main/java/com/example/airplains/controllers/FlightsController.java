package com.example.airplains.controllers;

import com.example.airplains.controllers.models.input.CheckInParameters;
import com.example.airplains.controllers.models.input.NewBookingParameters;
import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.repositories.FlightsRepository;
import com.example.airplains.servicies.BookingService;
import com.example.airplains.servicies.FlightsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("flights")
public class FlightsController {

    private final FlightsService flightsService;
    private final BookingService bookingService;
    private final FlightsRepository flights;

    public FlightsController(FlightsRepository flights, FlightsService flightsService, BookingService bookingService)
    {
        this.flights = flights;
        this.flightsService = flightsService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Flight> getAll() {
        return flightsService.getAllFlights();
    }

    @GetMapping("routes")
    public RouteDto getAllRoutes(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String departureDate,
            @RequestParam("fareCondition") FareConditions fareCondition,
            @RequestParam(required = false, defaultValue = "0") int connections) {
        return flightsService.getRoutes(from, to, departureDate, fareCondition, connections);
    }

    @PostMapping("bookings")
    public void bookRoute(@RequestBody NewBookingParameters parameters){
        this.bookingService.bookRoute(parameters);
    }

    @PostMapping("{flightId}/check-in")
    public void checkIn(@PathVariable Integer flightId, @RequestBody CheckInParameters parameters) {
        var flight = this.flights.findById(flightId);
        if (flight.isEmpty()){
            return;
        }

        this.bookingService.checkInForFlight(flightId, parameters.getTicketNo());
    }
}
