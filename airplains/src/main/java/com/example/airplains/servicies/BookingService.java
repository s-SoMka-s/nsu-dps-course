package com.example.airplains.servicies;

import com.example.airplains.controllers.models.input.NewBookingParameters;
import com.example.airplains.controllers.models.output.BookingDto;
import com.example.airplains.entities.BoardingPass;
import com.example.airplains.entities.Booking;
import com.example.airplains.entities.flights.Flight;
import com.example.airplains.entities.tickets.Ticket;
import com.example.airplains.entities.tickets.TicketFlight;
import com.example.airplains.repositories.BoardingPassesRepository;
import com.example.airplains.repositories.BookingRepository;
import com.example.airplains.repositories.TicketFlightsRepository;
import com.example.airplains.repositories.TicketsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<BookingDto> bookRoute(NewBookingParameters parameters){
        var newBooking = new Booking();
        newBooking.setTotal_amount(1.0);

        var insertedBooking = this.bookings.save(newBooking);

        var newTicket = new Ticket();
        newTicket.setBooking(insertedBooking);
        newTicket.setPassengerName(parameters.getPassengerName());

        var insertedTicket = this.tickets.save(newTicket);

        var res = new ArrayList<BookingDto>();
        for (var routNode : parameters.getNodes()) {
            var newTicketFlight = new TicketFlight();
            newTicketFlight.setTicketNo(insertedTicket.getTicketNo());
            newTicketFlight.setFlightId(routNode.getFlightId());
            newTicketFlight.setAmount(routNode.getAmount());
            newTicketFlight.setFareCondition(routNode.getFareConditions());

            var insertedTicketFlight = this.ticketFlights.save(newTicketFlight);

            res.add(new BookingDto(insertedTicketFlight.getFlightId(), insertedTicketFlight.getTicketNo()));
        }

        return res;
    }

    public BoardingPass checkInForFlight(TicketFlight ticketFlight) {
        var boardingPasses = this.boardingPasses.findAllByTicketNoAndFlightId(ticketFlight.getTicketNo(), ticketFlight.getFlightId());
        var boardingNo = getBoardingNo(boardingPasses);
        var seatNo = getSeatNo();

        var newBoardingPass = new BoardingPass();
        newBoardingPass.setFlightId(ticketFlight.getFlightId());
        newBoardingPass.setTicketNo(ticketFlight.getTicketNo());
        newBoardingPass.setSeatNo(seatNo);
        newBoardingPass.setBoardingNo(boardingNo);

        var inserted = this.boardingPasses.save(newBoardingPass);
        return inserted;
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
