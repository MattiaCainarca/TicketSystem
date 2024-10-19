package ch.supsi.webapp.tickets.repository;

import ch.supsi.webapp.tickets.model.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Repository
public class InMemoryRepository {
    private List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> findAll() {
        return tickets;
    }

    public Optional<Ticket> getOne(@PathVariable String id) {
        tickets.forEach(System.out::println);
        return tickets.stream().filter(bp -> Objects.equals(bp.getId(), id)).findFirst();
    }

    public Ticket create(Ticket ticket) {
        ticket.setId(UUID.randomUUID().toString());
        tickets.add(ticket);
        return ticket;
    }

    public Ticket update(String id, Ticket ticket) {
        Optional<Ticket> dbTicket = getOne(id);
        if (dbTicket.isEmpty())
            return null;
        tickets.remove(dbTicket.get());
        ticket.setId(id);
        tickets.add(ticket);
        return ticket;
    }

    public Boolean delete(String id) {
        Optional<Ticket> post = getOne(id);
        if (!post.isPresent())
            return false;
        return tickets.remove(post.get());
    }
}
