package com.example.airplains.repositories;

import com.example.airplains.entities.tickets.TicketFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TicketFlightsRepository extends JpaRepository<TicketFlight, String> {
    @Query("select tf from TicketFlight tf where " +
            "tf.ticketNo = ?1 and tf.flightId = ?2"
    )
    TicketFlight findByTicketNoAndFlightId(String ticketNo, Integer flightId);
}
