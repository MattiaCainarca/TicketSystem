package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Role;
import ch.supsi.webapp.tickets.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    public void save(Role roleUser) {
        this.roleRepository.save(roleUser);
    }
}
