package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Status;
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
                    ticket.setStatus(ticketData.getStatus());
                    ticket.setTitle(ticketData.getTitle());
                    ticket.setDescription(ticketData.getDescription());
                    ticket.setDueDate(ticketData.getDueDate());
                    ticket.setTimeEstimate(ticketData.getTimeEstimate());
                    ticket.setType(ticketData.getType());
                    ticket.setUserAssignee(ticketData.getUserAssignee());
                    return ticketRepository.save(ticket);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticket.ifPresent(ticketRepository::delete);
    }

    public List<Ticket> searchTickets(String query) {
        return ticketRepository.findInTickets(query);
    }

    public void updateTimeSpent(Long id, Ticket ticketData) {
        ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTimeSpent(ticketData.getTimeSpent());
                    return ticketRepository.save(ticket);
                });
    }

    public long getNumOfTicketsByState(Status state) {
        return ticketRepository.findAll().stream().filter(ticket -> ticket.getStatus().equals(state)).count();
    }
}
