package ch.supsi.webapp.tickets.controller;

import ch.supsi.webapp.tickets.model.*;
import ch.supsi.webapp.tickets.service.TicketService;
import ch.supsi.webapp.tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

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
    public String index(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "index";
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
                               @RequestParam Long userId,
                               @RequestParam("files") MultipartFile[] attachments) throws IOException {
        User user = userService.findById(userId);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        ticket.setUser(user);

        for (MultipartFile file : attachments)
            if (!file.isEmpty()) {
                Attachment attachment = Attachment.builder()
                        .bytes(file.getBytes())
                        .name(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .build();
                ticket.getAttachments().add(attachment);
            }

        ticketService.create(ticket);
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
