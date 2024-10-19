package ch.supsi.webapp.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Success {
    private boolean success = true;
}
