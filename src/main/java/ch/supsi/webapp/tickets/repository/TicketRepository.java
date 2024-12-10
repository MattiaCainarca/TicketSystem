package ch.supsi.webapp.tickets.repository;

import ch.supsi.webapp.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t from Ticket t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(t.user.username) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Ticket> findInTickets(@Param("q") String q);
}