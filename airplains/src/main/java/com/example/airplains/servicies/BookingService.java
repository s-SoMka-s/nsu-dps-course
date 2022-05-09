package com.example.airplains.servicies;

import com.example.airplains.controllers.models.input.NewBookingParameters;
import com.example.airplains.entities.BoardingPass;
import com.example.airplains.entities.Booking;
import com.example.airplains.entities.tickets.Ticket;
import com.example.airplains.entities.tickets.TicketFlight;
import com.example.airplains.repositories.BoardingPassesRepository;
import com.example.airplains.repositories.BookingRepository;
import com.example.airplains.repositories.TicketFlightsRepository;
import com.example.airplains.repositories.TicketsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final TicketsRepository tickets;
    private final TicketFlightsRepository ticketFlights;
    private final BoardingPassesRepository boardingPasses;

    public BookingService(BookingRepository bookings, TicketsRepository tickets, TicketFlightsRepository ticketFlights, BoardingPassesRepository boardingPasses) {
        this.bookings = bookings;
        this.tickets = tickets;
        this.ticketFlights = ticketFlights;
        this.boardingPasses = boardingPasses;
    }

    public void bookRoute(NewBookingParameters parameters){
        var newBooking = new Booking();
        newBooking.setTotal_amount(1.0);

        var insertedBooking = this.bookings.save(newBooking);

        var newTicket = new Ticket();
        newTicket.setBooking(insertedBooking);
        newTicket.setPassengerName(parameters.getPassengerName());

        var insertedTicket = this.tickets.save(newTicket);

        for (var routNode : parameters.getNodes()) {
            var newTicketFlight = new TicketFlight();
            newTicketFlight.setTicketNo(insertedTicket.getTicketNo());
            newTicketFlight.setFlightId(routNode.getFlightId());
            newTicketFlight.setAmount(routNode.getAmount());
            newTicketFlight.setFareCondition(routNode.getFareConditions());

            var insertedTicketFlight = this.ticketFlights.save(newTicketFlight);
        }
    }

    public void checkInForFlight(Integer flightId, String ticketNum) {
        var boardingPasses = this.boardingPasses.findAllByTicketNoAndFlightId(ticketNum, flightId);
        var boardingNo = getBoardingNo(boardingPasses);
        var seatNo = getSeatNo();

        var newBoardingPass = new BoardingPass();
        newBoardingPass.setFlightId(flightId);
        newBoardingPass.setTicketNo(ticketNum);
        newBoardingPass.setSeatNo(seatNo);
        newBoardingPass.setBoardingNo(boardingNo);

        var inserted = this.boardingPasses.save(newBoardingPass);
    }

    private int getBoardingNo(List<BoardingPass> passes){
        if (passes.size() > 0) {
            return passes.get(passes.size() - 1).getBoardingNo() + 1;
        }

        return 1;
    }

    private String getSeatNo(){
        return "1A";
    }
}
