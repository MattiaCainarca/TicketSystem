package ch.supsi.webapp.tickets.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.io.FileUtils;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] bytes;

    @Column(name = "attachment_name")
    private String name;

    private String contentType;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public String getReadableSize() {
        return FileUtils.byteCountToDisplaySize(size);
    }
}
