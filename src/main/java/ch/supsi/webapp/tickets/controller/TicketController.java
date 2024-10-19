package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.Success;
import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("")
    public ResponseEntity<Ticket> postTicket(@RequestBody Ticket newTicket) {
        return new ResponseEntity<>(ticketService.create(newTicket), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String id) {
        Optional<Ticket> ticket = ticketService.getOne(id);
        return ticket.isPresent() ? new ResponseEntity<>(ticket.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable String id, @RequestBody Ticket updatedTicket) {
        return ticketService.update(id, updatedTicket) != null ? ResponseEntity.ok(updatedTicket) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Success> deleteTicket(@PathVariable String id) {
        boolean removed = ticketService.delete(id);
        return removed ? new ResponseEntity<>(Success.builder().success(true).build(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

