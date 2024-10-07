package ch.supsi.webapp;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Ticket {
    private String id;
    private String title;
    private String description;
    private String author;

    public Ticket() {
        this.id = UUID.randomUUID().toString();
    }

    public Ticket(String title, String description, String author) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.author = author;
    }
}
