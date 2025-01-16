package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tickets")
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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tag_ticket",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private List<Tag> tags = new ArrayList<>();

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
