package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.*;
import ch.supsi.webapp.tickets.service.CustomerUserDetailService;
import ch.supsi.webapp.tickets.service.RoleService;
import ch.supsi.webapp.tickets.service.TicketService;
import ch.supsi.webapp.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TicketController {
    private final PasswordEncoder passwordEncoder;
    private final TicketService ticketService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TicketController(PasswordEncoder passwordEncoder, TicketService ticketService, UserService userService, CustomerUserDetailService customerUserDetailService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.ticketService = ticketService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(String username, String password, String firstName, String lastName, Model model) {
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username is already taken!");
            return "register";
        }

        Role userRole = roleService.findByName("ROLE_USER");
        if (userRole == null)
            roleService.save(new Role("ROLE_USER"));

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newUser.setRoles(roles);

        userService.save(newUser);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/ticket/{id}")
    public String ticketDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        return "ticketDetails";
    }

    @GetMapping("/ticket/new")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("types", Type.values());
        model.addAttribute("users", userService.findAll());
        return "createTicketForm";
    }

    @PostMapping("/ticket/new")
    public String createTicket(@ModelAttribute Ticket ticket,
                               @RequestParam("files") MultipartFile[] attachments) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username).orElse(null);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authenticated user not found!");
        ticket.setUser(user);

        ticket = ticketService.save(ticket);

        for (MultipartFile file : attachments)
            if (!file.isEmpty()) {
                Attachment attachment = Attachment.builder()
                        .bytes(file.getBytes())
                        .name(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .ticket(ticket)
                        .build();
                ticket.getAttachments().add(attachment);
            }

        ticketService.update(ticket.getId(), ticket);
        return "redirect:/";
    }

    @GetMapping(value = "/ticket/{id}/attachment/{attachmentName}")
    @ResponseBody
    public ResponseEntity<byte[]> getAttachmentBytes(@PathVariable Long id, @PathVariable String attachmentName) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found!");

        Attachment attachment = ticket.getAttachments().stream()
                .filter(a -> a.getName().equals(attachmentName))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found!"));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .header("Content-Disposition", "inline; filename=\"" + attachment.getName() + "\"")
                .body(attachment.getBytes());
    }

    @GetMapping("/ticket/{id}/edit")
    public String editTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found!");
        model.addAttribute("ticket", ticket);
        model.addAttribute("types", Type.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("users", userService.findAll());
        return "editTicket";
    }

    @PostMapping("/ticket/{id}/edit")
    public String editTicket(@PathVariable Long id, @ModelAttribute Ticket ticket, @RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        ticket.setUser(user);
        ticketService.update(id, ticket);
        return "redirect:/";
    }

    @GetMapping("/ticket/{id}/delete")
    public String deleteTicket(@PathVariable Long id, Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        ticketService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/tickets/search")
    @ResponseBody
    public List<Ticket> searchTickets(@RequestParam("q") String query) {
        return ResponseEntity.ok(ticketService.searchTickets(query)).getBody();
    }
}
