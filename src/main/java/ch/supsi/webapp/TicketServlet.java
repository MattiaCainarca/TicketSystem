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

@WebServlet(value = "/tickets/*")
public class TicketServlet extends HttpServlet {
    private final List<Ticket> tickets = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Ticket ticket;
        if (req.getContentType().equals("application/json")) {
            try {
                ticket = mapper.readValue(req.getReader(), Ticket.class);
                tickets.add(ticket);
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.setContentType(req.getContentType());
                resp.getWriter().write(mapper.writeValueAsString(tickets));
            } catch (UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Invalid or missing fields: title, description, author\"}");
            }
        } else if (req.getContentType().equals("application/x-www-form-urlencoded")) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String author = req.getParameter("author");

            if (title != null && description != null && author != null) {
                ticket = new Ticket(title, description, author);
                tickets.add(ticket);
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(tickets));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Missing required fields: title, description, author\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); // 415 Unsupported Media Type
            resp.setContentType("application/json");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
            resp.getWriter().write(mapper.writeValueAsString(tickets));
        } else {
            int index = getTicketIndexFromPath(pathInfo, resp);
            if (index != -1) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
                resp.getWriter().write(mapper.writeValueAsString(tickets.get(index)));
            }
        }
    }

    private int getTicketIndexFromPath(String pathInfo, HttpServletResponse resp) throws IOException {
        try {
            String[] pathParts = pathInfo.split("/");
            int index = Integer.parseInt(pathParts[1]);
            if (index >= 0 && index < tickets.size()) {
                return index;
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                resp.getWriter().write("{\"error\": \"Ticket not found.\"}");
                return -1;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("{\"error\": \"Invalid ticket ID.\"}");
            return -1;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Ticket ID is required for update.\"}");
            return;
        }
        int index = getTicketIndexFromPath(pathInfo, resp);
        if (index != -1) {
            try {
                Ticket updatedTicket = mapper.readValue(req.getReader(), Ticket.class);
                tickets.set(index, updatedTicket);
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(tickets.get(index)));
            } catch (UnrecognizedPropertyException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\": \"Invalid data for updating the ticket.\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Ticket ID is required for deletion.\"}");
            return;
        }
        int index = getTicketIndexFromPath(pathInfo, resp);
        if (index != -1) {
            tickets.remove(index);
            resp.getWriter().write("{\"message\": \"Ticket deleted successfully.\"}");
        }
    }


}
