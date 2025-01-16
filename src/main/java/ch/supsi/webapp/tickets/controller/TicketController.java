package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.*;
import ch.supsi.webapp.tickets.service.*;
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
    private final MilestoneService milestoneService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TicketController(PasswordEncoder passwordEncoder, TicketService ticketService, MilestoneService milestoneService, UserService userService, CustomerUserDetailService customerUserDetailService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.ticketService = ticketService;
        this.milestoneService = milestoneService;
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
        return "redirect:/ticket/" + ticket.getId();
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
        return "redirect:/ticket/" + ticket.getId();
    }

    @GetMapping("/ticket/{id}/delete")
    public String deleteTicket(@PathVariable Long id, Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        ticketService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/tickets/search")
    @ResponseBody
    public ResponseEntity<?> searchTickets(@RequestParam(value = "q", required = false) String query) {
        if (query == null || query.trim().isEmpty())
            return ResponseEntity.ok(ticketService.findAll());

        if (query.trim().length() < 3)
            return ResponseEntity.badRequest().body("Type at least 3 characters to search.");

        return ResponseEntity.ok(ticketService.searchTickets(query.trim()));
    }

    @GetMapping("/milestone")
    public String milestone(Model model) {
        model.addAttribute("milestones", milestoneService.findAll());
        return "milestone";
    }

    @GetMapping("/milestone/new")
    public String createMilestoneForm(Model model) {
        model.addAttribute("milestone", new Milestone());
        model.addAttribute("users", userService.findAll());
        return "createMilestoneForm";
    }

    @PostMapping("/milestone/new")
    public String createMilestone(@ModelAttribute Milestone milestone) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username).orElse(null);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authenticated user not found!");
        milestone.setAuthor(user);

        milestoneService.save(milestone);

        return "redirect:/milestone";
    }

    @GetMapping("/milestone/{id}/completed")
    public ResponseEntity<?> markComplete(@PathVariable Long id) {
        Milestone milestone = milestoneService.findById(id);
        if (milestone == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found!");

        milestoneService.complete(milestone);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/milestone/{id}/assignTicket")
    public String assignTicket(@PathVariable Long id, Model model) {
        Milestone milestone = milestoneService.findById(id);
        if (milestone == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found!");
        model.addAttribute("milestone", milestone);
        model.addAttribute("tickets", ticketService.findAll());
        return "addTicketToMilestone";
    }

    @PostMapping("milestone/{id}/assignTicket")
    public String assignTicketForm(@PathVariable Long id, @RequestParam Long ticketId) {
        Milestone milestone = milestoneService.findById(id);
        if (milestone == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found!");

        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found!");

        milestoneService.addTicket(milestone, ticket);
        milestoneService.update(id, milestone);

        System.out.println(milestone.getTickets().size());
        return "redirect:/milestone";
    }

    @GetMapping("/ticket/{id}/changeState")
    public ResponseEntity<?> changeState(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found!");
        System.out.println(ticket.getStatus());

        if (ticket.getStatus() == Status.OPEN)
            ticket.setStatus(Status.IN_PROGRESS);
        else if (ticket.getStatus() == Status.IN_PROGRESS)
            ticket.setStatus(Status.DONE);
        else if (ticket.getStatus() == Status.DONE)
            ticket.setStatus(Status.CLOSED);
        else
            ticket.setStatus(Status.OPEN);

        ticketService.update(id, ticket);
        System.out.println(ticket.getStatus());
        return ResponseEntity.ok(ticket.getStatus());
    }
}