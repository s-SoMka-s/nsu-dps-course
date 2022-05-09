package com.example.airplains.repositories;

import com.example.airplains.entities.BoardingPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardingPassesRepository extends JpaRepository<BoardingPass, String> {
    List<BoardingPass> findAllByTicketNoAndFlightId(String ticketNo, Integer flightId);
}
