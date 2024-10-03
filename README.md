# Ticket Management REST API

## Description
This REST API allows clients to manage tickets, which include a title, description, and author. 


## URIs

### 1. Create a Ticket
- **POST** `/tickets`
- **Description**: Creates a new ticket.
- **Request Headers**:
    - `Content-Type`: `application/json` or `application/x-www-form-urlencoded`
- **Request Body (application/json)**:
```json
{
    "title": "Ticket Title",
    "description": "Detailed description of the ticket",
    "author": "Author Name"
}

**Code** : `201 CREATED`

{
  "title": "Ticket Title",
  "description": "Detailed description of the ticket",
  "author": "Author Name"
}

**Code** : `400 BAD REQUEST`

{
  "error": "Missing required fields: title, description, author"
}
