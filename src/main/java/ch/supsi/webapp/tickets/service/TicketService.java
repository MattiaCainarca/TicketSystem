package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.model.User;
import ch.supsi.webapp.tickets.repository.TicketRepository;
import ch.supsi.webapp.tickets.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getOne(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket create(Ticket ticket) {
        Optional<User> existingUser = userRepository.findByUsername(ticket.getUser().getUsername());
        if (existingUser.isPresent())
            ticket.setUser(existingUser.get());
        else
            userRepository.save(ticket.getUser());
        return ticketRepository.save(ticket);
    }

    public Ticket update(Long id, Ticket ticket) {
        Optional<Ticket> updatedTicket = ticketRepository.findById(id);
        if (updatedTicket.isPresent()) {
            ticket.setId(id);
            User user = checkUser(ticket.getUser());
            ticket.setUser(user);
            return ticketRepository.save(ticket);
        } else
            return null;
    }

    public boolean delete(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            ticketRepository.delete(ticket.get());
            return true;
        }
        return false;
    }

    private User checkUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            user.setId(existingUser.get().getId());
            return user;
        } else
            return userRepository.save(user);
    }
}
