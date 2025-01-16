package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Milestone;
import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.repository.MilestoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestoneService {
    private final MilestoneRepository milestoneRepository;

    public MilestoneService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    public Milestone save(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    public List<Milestone> findAll() {
        return milestoneRepository.findAll();
    }

    public Milestone findById(Long id) {
        return milestoneRepository.findById(id).orElse(null);
    }

    public void complete(Milestone milestone) {
        milestone.setCompleted(true);
        milestoneRepository.save(milestone);
    }

    public void addTicket(Milestone milestone, Ticket ticket) {
        milestone.addTicket(ticket);
    }

    public void update(Long id, Milestone milestoneData) {
        milestoneRepository.findById(id)
                .map(milestone -> {
                    milestone.setTickets(milestoneData.getTickets());
                    return milestoneRepository.save(milestoneData);
                })
                .orElse(null);
    }
}
