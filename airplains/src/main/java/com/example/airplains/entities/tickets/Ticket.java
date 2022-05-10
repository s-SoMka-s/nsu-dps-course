package com.example.airplains.entities.tickets;

import com.example.airplains.entities.Booking;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

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

//    @Type(type = "jsonb")
//    @Column(name = "contact_data", columnDefinition = "jsonb")
//    private String contactData;

    public Ticket() {
        this.ticketNo =  "_" + UUID.randomUUID().toString().substring(0, 12);
        this.passengerId = UUID.randomUUID().toString().substring(0, 20);
        this.passengerName = "";
        //this.contactData = null;
        this.booking = null;
    }
}
