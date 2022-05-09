package com.example.airplains.entities.tickets;

import com.example.airplains.entities.flights.FareConditions;
import com.example.airplains.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ticket_flights")
public class TicketFlight {

    @Id
    @PrimaryKeyJoinColumn(name = "ticket_no", referencedColumnName = "ticket_no")
    private String ticketNo;

    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "fare_conditions")
    private FareConditions fareCondition;

    @Column(name = "amount")
    private double amount;
}
