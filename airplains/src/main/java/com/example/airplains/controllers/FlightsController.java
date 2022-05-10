package com.example.airplains.controllers;

import com.example.airplains.controllers.models.input.CheckInParameters;
import com.example.airplains.controllers.models.input.NewBookingParameters;
import com.example.airplains.controllers.models.output.BookingDto;
import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.entities.BoardingPass;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.repositories.*;
import com.example.airplains.servicies.BookingService;
import com.example.airplains.servicies.FlightsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("flights")
public class FlightsController {

    private final FlightsService flightsService;
    private final BookingService bookingService;
    private final AirportsRepository airports;
    private final BoardingPassesRepository boardingPasses;
    private final TicketFlightsRepository ticketFlights;

    public FlightsController(
            FlightsService flightsService,
            BookingService bookingService,
            AirportsRepository airports,
            BoardingPassesRepository boardingPasses,
            TicketFlightsRepository ticketFlights) {
        this.flightsService = flightsService;
        this.bookingService = bookingService;
        this.airports = airports;
        this.boardingPasses = boardingPasses;
        this.ticketFlights = ticketFlights;
    }

    @GetMapping("cities")
    public List<String> getAll() {
        return this.airports.findAllCities();
    }

    @GetMapping("routes")
    public RouteDto getAllRoutes(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String departureDate,
            @RequestParam("bookingClass") FareConditions fareCondition,
            @RequestParam(required = false, defaultValue = "0") int connections) {
        return flightsService.getRoutes(from, to, departureDate, fareCondition, connections);
    }

    @PostMapping("bookings")
    public List<BookingDto> bookRoute(@RequestBody NewBookingParameters parameters){
        return this.bookingService.bookRoute(parameters);
    }

    @PostMapping("{flightId}/check-in")
    public BoardingPass checkIn(@PathVariable Integer flightId, @RequestBody CheckInParameters parameters) {
        var ticketFlight = this.ticketFlights.findByTicketNoAndFlightId(parameters.getTicketNo(), flightId);
        if (ticketFlight == null) {
            return null;
        }

        var boardingPasses = this.boardingPasses.findAllByTicketNoAndFlightId(parameters.getTicketNo(), flightId);
        if (!boardingPasses.isEmpty()) {
            return null;
        }

        return this.bookingService.checkInForFlight(ticketFlight);
    }
}
