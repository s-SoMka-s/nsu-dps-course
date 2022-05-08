package com.example.airplains.repositories;

import com.example.airplains.entities.tickets.TicketFlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketFlightsRepository extends JpaRepository<TicketFlight, String> {
}
