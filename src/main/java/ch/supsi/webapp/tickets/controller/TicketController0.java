package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController0 {
    private final TicketService ticketService;

    @Autowired
    public TicketController0(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("")
    public ResponseEntity<Ticket> postTicket(@RequestBody Ticket newTicket) {
        Ticket createdTicket = ticketService.create(newTicket);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        return ticket != null ? new ResponseEntity<>(ticket, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket updatedTicket) {
        return ticketService.update(id, updatedTicket) != null ? ResponseEntity.ok(updatedTicket) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        if (ticketService.findById(id) != null)
            ticketService.delete(id);
    }
}

