package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDate;

    @Column()
    private boolean completed;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "milestone_ticket",
            joinColumns = @JoinColumn(name = "milestone_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private List<Ticket> tickets = new ArrayList<>();

    public Milestone() {
        this.creationDate = new Date();
        this.completed = false;
    }

    public Milestone(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public float progressCalc() {
        float count = 0;
        for (Ticket ticket : tickets)
            if (ticket.getStatus() == Status.CLOSED)
                count++;
        if (count == 0)
            return 0f;
        return count / tickets.size() * 100;
    }
}
