package com.example.airplains.entities.flights;

import com.example.airplains.entities.Airport;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.OffsetDateTime;

import static com.example.airplains.tools.utils.DestinationUtils.isAirportCode;


@Getter
@Setter
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @Column(name = "flight_id")
    private int id;

    @Column(name = "flight_no")
    private String flightNo;

    @Column(name = "scheduled_departure")
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private OffsetDateTime scheduledArrival;

    @ManyToOne
    @JoinColumn(name = "departure_airport", referencedColumnName = "airport_code")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport", referencedColumnName = "airport_code")
    private Airport arrivalAirport;

    @Column(name = "status")
    private String status;

    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Column(name = "actual_departure")
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private OffsetDateTime actualArrival;

    public String getArrivalCity() {
        return this.getArrivalAirport().getCity();
    }

    public String getDepartureCity() {
        return this.getDepartureAirport().getCity();
    }

    public boolean arrivesTo(String destination) {
        if (isAirportCode(destination)) {
            return this.getArrivalAirport().getAirportCode().equals(destination);
        }

        return this.getArrivalAirport().getCity().equals(destination);
    }

    public boolean fliesOutOf(String source) {
        if (isAirportCode(source)) {
            return this.getDepartureAirport().getAirportCode().equals(source);
        }

        return this.getDepartureAirport().getCity().equals(source);
    }
}