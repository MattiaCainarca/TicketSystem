package ch.supsi.webapp.tickets.service;

import ch.supsi.webapp.tickets.model.Role;
import ch.supsi.webapp.tickets.model.User;
import ch.supsi.webapp.tickets.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException("User not found");
        List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(user.getRoles().stream().map(Role::getName).toList());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true, true, auth);
    }
}
