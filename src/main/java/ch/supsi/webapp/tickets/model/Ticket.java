package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column()
    private Status status;

    @Column()
    private LocalDateTime createdDate;

    public Ticket() {
        this.createdDate = LocalDateTime.now();
        this.status = Status.OPEN;
    }

    public Ticket(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.createdDate = LocalDateTime.now();
        this.status = Status.OPEN;
    }
}
