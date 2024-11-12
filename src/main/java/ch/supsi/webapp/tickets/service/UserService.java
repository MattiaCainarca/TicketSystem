package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.model.User;
import ch.supsi.webapp.tickets.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
