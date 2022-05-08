package com.example.airplains.entities.tickets;

import com.example.airplains.entities.Booking;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "ticket_no")
    private String ticketNo;

    @ManyToOne
    @JoinColumn(name = "book_ref", referencedColumnName = "book_ref")
    private Booking booking;

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "contact_data")
    private String contactData;
}
