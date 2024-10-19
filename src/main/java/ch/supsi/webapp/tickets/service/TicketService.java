package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.repository.InMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final InMemoryRepository repository;

    public TicketService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Optional<Ticket> getOne(String id) {
        return repository.getOne(id);
    }

    public Ticket create(Ticket ticket) {
        return repository.create(ticket);
    }

    public Ticket update(String id, Ticket ticket) {
        return repository.update(id, ticket);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }
}
