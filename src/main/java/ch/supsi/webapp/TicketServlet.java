package ch.supsi.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/tickets")
public class TicketServlet extends HttpServlet {

    List<Ticket> tickets = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Ticket ticket;
        if (req.getContentType().equals("application/json")) {
            try {
                ticket = mapper.readValue(req.getReader(), Ticket.class);
                tickets.add(ticket);
                resp.setContentType(req.getContentType());
                resp.getWriter().write(mapper.writeValueAsString(tickets));
            } catch (UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing required fields: title, description, author\"}");
            }
        } else if (req.getContentType().equals("application/x-www-form-urlencoded")) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String author = req.getParameter("author");

            if (title != null && description != null && author != null) {
                ticket = new Ticket(title, description, author);
                tickets.add(ticket);
                resp.getWriter().write(mapper.writeValueAsString(tickets));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing required fields: title, description, author\"}");
            }
        } else
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(tickets));
    }
}
