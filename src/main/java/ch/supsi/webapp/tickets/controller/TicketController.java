package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.Status;
import ch.supsi.webapp.tickets.model.Ticket;
import ch.supsi.webapp.tickets.model.Type;
import ch.supsi.webapp.tickets.model.User;
import ch.supsi.webapp.tickets.service.TicketService;
import ch.supsi.webapp.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("tickets", ticketService.findAll());
        return "index";
    }

    @GetMapping("/ticket/{id}")
    public String ticketDetails(@PathVariable("id") Long id, Model model){
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
    public String createTicket(@ModelAttribute Ticket ticket, @RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        ticket.setUser(user);
        ticketService.create(ticket);
        return "redirect:/";
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
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        ticket.setUser(user);
        ticketService.update(id, ticket);
        return "redirect:/";
    }

    @GetMapping("/ticket/{id}/delete")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.delete(id);
        return "redirect:/";
    }
}
