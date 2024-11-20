package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private Type type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column()
    private Status status;

    @Column()
    private Date createdDate;

    @ElementCollection
    @CollectionTable(name = "ticket_attachments", joinColumns = @JoinColumn(name = "attachment_name"))
    private List<Attachment> attachments = new ArrayList<>();

    public Ticket() {
        this.createdDate = new Date();
        this.status = Status.OPEN;
    }

    public Ticket(String title, Type type, String description, User user) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.user = user;
        this.createdDate = new Date();
        this.status = Status.OPEN;
    }
}
