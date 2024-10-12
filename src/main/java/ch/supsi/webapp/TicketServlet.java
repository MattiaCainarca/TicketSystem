package ch.supsi.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.ServletException;
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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH"))
            doPatch(req, resp);
        else
            super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getContentType().equals("application/json"))
            try {
                created(resp, mapper.readValue(req.getReader(), Ticket.class));
            } catch (UnrecognizedPropertyException e) {
                badRequest(resp, "Invalid or missing fields: title, description, author.");
            }
        else if (req.getContentType().equals("application/x-www-form-urlencoded")) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String author = req.getParameter("author");

            if (title != null && description != null && author != null)
                created(resp, new Ticket(title, description, author));
            else
                badRequest(resp, "Missing required fields: title, description, author.");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); // 415 Unsupported Media Type
            resp.setContentType("application/json");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/"))
            success(resp, tickets);
        else {
            int index = getTicketIndexFromPath(pathInfo, resp);
            if (index != -1)
                success(resp, tickets.get(index));
        }
    }

    private int getTicketIndexFromPath(String pathInfo, HttpServletResponse resp) throws IOException {
        String[] pathParts = pathInfo.split("/");
        String id = pathParts[1];
        for (int i = 0; i < tickets.size(); i++)
            if (tickets.get(i).getId().equals(id))
                return i;
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
        resp.getWriter().write("{\"error\": \"Ticket not found.\"}");
        return -1;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = checkPathInfo(req, resp, "{\"error\": \"Ticket ID is required for update.\"}");
        if (pathInfo == null)
            return;
        int index = getTicketIndexFromPath(pathInfo, resp);
        if (index != -1)
            try {
                Ticket updatedTicket = mapper.readValue(req.getReader(), Ticket.class);
                tickets.set(index, updatedTicket);
                success(resp, tickets.get(index));
            } catch (UnrecognizedPropertyException e) {
                badRequest(resp, "Invalid data for updating the ticket.");
            }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = checkPathInfo(req, resp, "{\"error\": \"Ticket ID is required for deletion.\"}");
        if (pathInfo == null)
            return;
        int index = getTicketIndexFromPath(pathInfo, resp);
        if (index != -1) {
            tickets.remove(index);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 NO CONTENT
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = checkPathInfo(req, resp, "{\"error\": \"Ticket ID is required for partial update.\"}");
        if (pathInfo == null)
            return;
        int index = getTicketIndexFromPath(pathInfo, resp);
        if (index != -1) {
            Ticket existingTicket = tickets.get(index);
            try {
                Ticket partialUpdate = mapper.readValue(req.getReader(), Ticket.class);
                if (partialUpdate.getTitle() != null)
                    existingTicket.setTitle(partialUpdate.getTitle());
                if (partialUpdate.getDescription() != null)
                    existingTicket.setDescription(partialUpdate.getDescription());
                if (partialUpdate.getAuthor() != null)
                    existingTicket.setAuthor(partialUpdate.getAuthor());
                success(resp, existingTicket);
            } catch (UnrecognizedPropertyException e) {
                badRequest(resp, "Invalid data for partial update.");
            }
        }
    }

    private static String checkPathInfo(HttpServletRequest req, HttpServletResponse resp, String s) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            badRequest(resp, s);
            return null;
        }
        return pathInfo;
    }

    private void success(HttpServletResponse resp, Object obj) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(obj));
    }

    private void created(HttpServletResponse resp, Ticket ticket) throws IOException {
        tickets.add(ticket);
        resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(tickets));
    }

    private static void badRequest(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        resp.setContentType("application/json");
        resp.getWriter().write("{\"error\": " + message + "}");
    }
}
