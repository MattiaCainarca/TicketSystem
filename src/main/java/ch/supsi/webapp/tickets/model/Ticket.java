package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User userAssignee;

    @Enumerated(EnumType.STRING)
    @Column()
    private Status status;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Column()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column()
    private float timeEstimate;

    @Column()
    private float timeSpent;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    public Ticket() {
        this.createdDate = new Date();
        this.status = Status.OPEN;
    }

    public Ticket(String title, Type type, Date dueDate, float timeEstimate, String description, User user, User userAssignee) {
        this.title = title;
        this.type = type;
        this.dueDate = dueDate;
        this.timeEstimate = timeEstimate;
        this.description = description;
        this.user = user;
        this.userAssignee = userAssignee;
        this.createdDate = new Date();
        this.status = Status.OPEN;
        this.timeSpent = 0;
    }
}
