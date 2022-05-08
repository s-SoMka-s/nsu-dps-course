package com.example.airplains.controllers.models.mappers;

import com.example.airplains.controllers.models.output.AirportInboundScheduleDto;
import com.example.airplains.controllers.models.output.AirportOutboundScheduleDto;
import com.example.airplains.controllers.models.output.routes.RouteNode;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.entities.tickets.TicketFlight;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class FlightMapper {
    private final SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

    public AirportInboundScheduleDto mapInboundSchedule(Flight flight) {
        return AirportInboundScheduleDto.builder()
                .departureAirport(flight.getDepartureAirport().getAirportCode())
                .flightNo(flight.getFlightNo())
                .scheduledArrival(flight.getScheduledArrival())
                .dayOfWeek(simpleDateformat.format(flight.getScheduledArrival()))
                .build();
    }

    public AirportOutboundScheduleDto mapOutboundSchedule(Flight flight) {
        return AirportOutboundScheduleDto.builder()
                .arrivalAirport(flight.getArrivalAirport().getAirportCode())
                .flightNo(flight.getFlightNo())
                .scheduledDeparture(flight.getScheduledDeparture())
                .dayOfWeek(simpleDateformat.format(flight.getScheduledDeparture()))
                .build();
    }

    public RouteNode mapRouteNode(Flight flight) {
        return RouteNode.builder()
                .from(flight.getDepartureAirport().getAirportCode())
                .to(flight.getArrivalAirport().getAirportCode())
                .cityFrom(flight.getDepartureAirport().getCity())
                .cityTo(flight.getArrivalAirport().getCity())
                .flightId(flight.getId())
                .build();
    }
}
