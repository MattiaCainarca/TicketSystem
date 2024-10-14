package ch.supsi.webapp.tickets;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class TicketController {
    private List<Ticket> tickets = new ArrayList<>();

    @RequestMapping(value = "/tickets", method = RequestMethod.POST)
    public ResponseEntity<Ticket> postTicket(@RequestBody Ticket newTicket) {
        newTicket.setId(UUID.randomUUID().toString());
        tickets.add(newTicket);
        return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ticket> getTicketById(@PathVariable String id) {
        for (Ticket ticket : tickets)
            if (Objects.equals(ticket.getId(), id))
                return new ResponseEntity<>(ticket, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Ticket> updateTicket(@PathVariable String id, @RequestBody Ticket updatedTicket) {
        for (Ticket ticket : tickets)
            if (Objects.equals(ticket.getId(), id)) {
                ticket.setTitle(updatedTicket.getTitle());
                ticket.setDescription(updatedTicket.getDescription());
                ticket.setAuthor(updatedTicket.getAuthor());
                return new ResponseEntity<>(ticket, HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Ticket> deleteTicket(@PathVariable String id) {
        for (Ticket ticket : tickets)
            if (Objects.equals(ticket.getId(), id)) {
                tickets.remove(ticket);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
