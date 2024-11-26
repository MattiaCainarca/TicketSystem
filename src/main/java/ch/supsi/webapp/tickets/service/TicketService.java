package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket update(Long id, Ticket ticketData) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTitle(ticketData.getTitle());
                    ticket.setType(ticketData.getType());
                    ticket.setDescription(ticketData.getDescription());
                    ticket.setUser(ticketData.getUser());
                    ticket.setStatus(ticketData.getStatus());
                    return ticketRepository.save(ticket);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.ifPresent(ticketRepository::delete);
    }
}
