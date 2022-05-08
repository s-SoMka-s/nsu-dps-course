package com.example.airplains.servicies;

import com.example.airplains.controllers.models.mappers.FlightMapper;
import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.repositories.AirportsRepository;
import com.example.airplains.repositories.FlightsRepository;
import com.example.airplains.repositories.TicketFlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.airplains.utils.DestinationUtils.isAirportCode;

@Service
public class FlightsService {
    private final AirportsRepository airports;
    private final FlightsRepository flights;
    private final FlightMapper flightMapper;
    private final TicketFlightsRepository ticketFlights;

    @Autowired
    public FlightsService(
            AirportsRepository airports,
            FlightsRepository flights,
            TicketFlightsRepository ticketFlights,
            FlightMapper flightMapper) {
        this.airports = airports;
        this.flights = flights;
        this.ticketFlights = ticketFlights;
        this.flightMapper = flightMapper;
    }

    public List<Flight> getAllFlights() {
        return this.flights.findAll();
    }

    public List<RouteDto> getRoutes(String from,
                                    String to,
                                    String rawDepartureDate,
                                    FareConditions fareCondition,
                                    int numberOfConnections) {
        var simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            var departureDateFrom = new Date(simpleDateFormat.parse(rawDepartureDate).getTime());
            var calendar = Calendar.getInstance();
            calendar.setTime(departureDateFrom);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            var departureDateTo = new Date(calendar.getTimeInMillis());

            return getAllAppropriateRoutes(from, to, departureDateFrom, departureDateTo, fareCondition, numberOfConnections);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<RouteDto> getAllAppropriateRoutes(String from, String to, Date departureDateFrom, Date departureDateTo, FareConditions fareCondition, int numberOfConnections) {
        var flightsFrom = getAllFlightsFromPointAndBetweenDates(from, departureDateFrom, departureDateTo);

        if (numberOfConnections == 0) {
            var r = flightsFrom.stream()
                    .filter(f -> f.arrivesTo(to))
                    .map(flightMapper::mapRouteNode)
                    .collect(Collectors.toList());

            return new ArrayList();
        }

        return new ArrayList();
    }

    private boolean arrivesTo(Flight flight, String destination) {
        if (isAirportCode(destination)) {
            return flight.getArrivalAirport().getAirportCode().equals(destination);
        }

        return flight.getArrivalAirport().getCity().equals(destination);
    }



    private HashSet<Flight> getAllFlightsFromPointAndBetweenDates(String from, Date departureDate, Date departureDatePlusOne) {
        if (isAirportCode(from)) {
            return new HashSet<>(this.flights.findAllByDepartureAirport(
                    from,
                    departureDate,
                    departureDatePlusOne));
        }

        return new HashSet<>(this.flights.findAllByDepartureCity(
                    from,
                    departureDate,
                    departureDatePlusOne));
    }
}
