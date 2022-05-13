package com.example.airplains.servicies;

import com.example.airplains.controllers.models.mappers.FlightMapper;
import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.controllers.models.output.routes.RouteNodeDto;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.repositories.AirportsRepository;
import com.example.airplains.repositories.FlightsRepository;
import com.example.airplains.tools.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.airplains.tools.utils.DestinationUtils.isAirportCode;

@Service
public class FlightsService {
    private final AirportsRepository airports;
    private final FlightsRepository flights;
    private final FlightMapper flightMapper;

    @Autowired
    public FlightsService(
            AirportsRepository airports,
            FlightsRepository flights,
            FlightMapper flightMapper) {
        this.airports = airports;
        this.flights = flights;
        this.flightMapper = flightMapper;
    }

    public RouteDto getRoutes(String from,
                              String to,
                              OffsetDateTime departureDateFrom,
                              FareConditions fareCondition,
                              int numberOfConnections) {
        var departureDateTo = DateUtils.addDays(departureDateFrom, 1);

        var visitedCities = new HashSet<String>();
        visitedCities.add(this.getCity(from));

        var rootNodes = getAllAppropriateRoutes(
                from,
                to,
                departureDateFrom,
                departureDateTo,
                fareCondition,
                numberOfConnections,
                visitedCities
        );

        return flightMapper.mapRouteDto(rootNodes, getAirport(to), getCity(to));
    }

    private List<RouteNodeDto> getAllAppropriateRoutes(
            String from,
            String to,
            OffsetDateTime departureDateFrom,
            OffsetDateTime departureDateTo,
            FareConditions fareCondition,
            int numberOfConnections,
            Set<String> visitedCities) {
        var flightsFrom = getAllFlightsFromPointAndBetweenDates(from, departureDateFrom, departureDateTo);

        if (numberOfConnections == 0) {
            return flightsFrom.stream()
                    .filter(f -> f.arrivesTo(to))
                    .filter(f -> !visitedCities.contains(f.getArrivalCity()))
                    .map(f -> this.flightMapper.mapRouteNode(f, fareCondition))
                    .collect(Collectors.toList());
        }

        var res = new ArrayList<RouteNodeDto>();
        for (var flight : flightsFrom) {
            // except already visited cities
            if (visitedCities.contains(flight.getArrivalCity())) {
                continue;
            }

            // except flying out of destination
            if (flight.fliesOutOf(to)) {
                continue;
            }

            /*var price = this.flightPrices.findPrice(
                    flight.getArrivalAirport().getAirportCode(),
                    flight.getDepartureAirport().getAirportCode(),
                    fareCondition);

            if (price == null) {
                return;
            }*/

            var childVisitedCities = new HashSet<>(visitedCities);
            childVisitedCities.add(flight.getArrivalCity());

            var children = getAllAppropriateRoutes(
                    flight.getArrivalAirport().getAirportCode(),
                    to,
                    flight.getScheduledArrival(),
                    DateUtils.addDays(flight.getScheduledArrival(), 1),
                    fareCondition,
                    numberOfConnections - 1,
                    childVisitedCities
            );

            if (children.size() == 0) {
                continue;
            }

            var routeNode = this.flightMapper.mapRouteNode(flight, children, fareCondition);

            res.add(routeNode);
        }

        return res;
    }

    private HashSet<Flight> getAllFlightsFromPointAndBetweenDates(String from, OffsetDateTime departureDate, OffsetDateTime departureDatePlusOne) {
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

    private String getCity(String source) {
        if (isAirportCode(source)) {
            return this.airports.findFirstByAirportCode(source).getCity();
        }

        return source;
    }

    private String getAirport(String source) {
        if (isAirportCode(source)) {
            return source;
        }

        return this.airports.findFirstByCity(source).getAirportCode();
    }
}
