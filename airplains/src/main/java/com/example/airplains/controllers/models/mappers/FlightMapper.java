package com.example.airplains.controllers.models.mappers;

import com.example.airplains.controllers.models.output.AirportInboundScheduleDto;
import com.example.airplains.controllers.models.output.AirportOutboundScheduleDto;
import com.example.airplains.controllers.models.output.routes.RouteDto;
import com.example.airplains.controllers.models.output.routes.RouteNodeDto;
import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlightMapper {
    private final SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

    public AirportInboundScheduleDto mapInboundSchedule(Flight flight) {
        return AirportInboundScheduleDto.builder()
                .departureAirport(flight.getDepartureAirport().getAirportCode())
                .flightNo(flight.getFlightNo())
                .status(flight.getStatus())
                .scheduledArrival(flight.getScheduledArrival())
                .dayOfWeek(simpleDateformat.format(flight.getScheduledArrival()))
                .build();
    }

    public AirportOutboundScheduleDto mapOutboundSchedule(Flight flight) {
        return AirportOutboundScheduleDto.builder()
                .arrivalAirport(flight.getArrivalAirport().getAirportCode())
                .flightNo(flight.getFlightNo())
                .status(flight.getStatus())
                .scheduledDeparture(flight.getScheduledDeparture())
                .dayOfWeek(simpleDateformat.format(flight.getScheduledDeparture()))
                .build();
    }

    public RouteDto mapRouteDto(List<RouteNodeDto> nodes, String destinationAirport, String destinationCity) {
        var root = nodes.get(0);

        return RouteDto.builder()
                .departureDate(root.getDepartureDate())
                .sourceAirport(root.getDepartureAirport())
                .sourceCity(root.getDepartureCity())
                .destinationAirport(destinationAirport)
                .destinationCity(destinationCity)
                .routes(nodes)
                .build();
    }

    public RouteNodeDto mapRouteNode(Flight flight, FareConditions fareCondition) {
        return mapRouteNode(flight, new ArrayList<>(), fareCondition);
    }

    public RouteNodeDto mapRouteNode(Flight flight, List<RouteNodeDto> children, FareConditions fareCondition) {
        return RouteNodeDto.builder()
                .children(children)
                .arrivalAirport(flight.getArrivalAirport().getAirportCode())
                .departureAirport(flight.getDepartureAirport().getAirportCode())
                .arrivalCity(flight.getArrivalCity())
                .departureCity(flight.getDepartureCity())
                .departureDate(flight.getScheduledDeparture())
                .arrivalDate(flight.getScheduledArrival())
                .flightId(flight.getId())
                .fareConditions(fareCondition)
                .build();
    }
}
