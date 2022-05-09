package com.example.airplains.repositories;

import com.example.airplains.entities.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketsRepository extends JpaRepository<Ticket, String> {
}
