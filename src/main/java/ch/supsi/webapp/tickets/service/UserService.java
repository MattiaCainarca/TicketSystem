package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.model.User;
import ch.supsi.webapp.tickets.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addToWatchlist(User user, Ticket ticket) {
        user.addTicketToWatchlist(ticket);
        userRepository.save(user);
    }

    public Set<Ticket> getWatchlist(User user) {
        return user.getWatchlistTickets();
    }

    public boolean checkTicketWatched(User user, Ticket id) {
        return user.getWatchlistTickets().contains(id);
    }
}
