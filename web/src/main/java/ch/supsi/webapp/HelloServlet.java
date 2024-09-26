package ch.supsi.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/tickets")
public class HelloServlet extends HttpServlet {

    List<Ticket> tickets = new ArrayList<Ticket>();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Ticket ticket = null;
        if (req.getContentType().equals("application/json")) {
            ticket = mapper.readValue(req.getReader(), Ticket.class);
            tickets.add(ticket);
            resp.setContentType(req.getContentType());
            resp.getWriter().write(mapper.writeValueAsString(tickets));
        } else if (req.getContentType().equals("application/x-www-form-urlencoded")) {
            ticket = new Ticket(req.getParameter("title"),
                    req.getParameter("description"),
                    req.getParameter("author"));
            tickets.add(ticket);
            resp.setContentType(req.getContentType());
            resp.getWriter().write(mapper.writeValueAsString(tickets));
        } else
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(tickets));
    }
}
