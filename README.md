# Ticket Management REST API

## Description
This REST API allows clients to manage tickets, which include a title, description, and author.

## Base URL : `/ticket`

## URIs

## 1. Create a Ticket
- **Method** : `POST`
- **URL** : `/tickets`
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
    ```
- ### Success Response
    **Code** : `201 CREATED`
    ```json
    {
        "title": "Ticket Title",
        "description": "Detailed description of the ticket",
        "author": "Author Name"
    }
    ```
- ### Error Response
    **Code** : `400 BAD REQUEST`  
    **Condition** : If some fields are missing.
    ```json
    {
        "error": "Missing required fields: title, description, author"
    }
    ```

## 2. List all Tickets
- **Method** : `GET`
- **URL** : `/tickets`
- **Description**: Retrieves a list of all tickets.
- **Request Headers**:
    - `Content-Type`: `application/json`
- ### Success Response
    **Code** : `200 OK`  
    ```json
    [
      {
          "title": "Ticket 1",
          "description": "Description 1",
          "author": "Author 1"
      },
      {
          "title": "Ticket 2",
          "description": "Description 2",
          "author": "Author 2"
      }
    ]
    ```

## 3. Get a specific Ticket
- **Method** : `GET`
- **URL** : `/tickets/{id}`
- **Description**: Retrieves a specific ticket by ID.
- **Request Headers**:
    - `Content-Type`: `application/json`
- ### Success Response
    **Code** : `200 OK`
    ```json
    {
        "title": "Ticket Title",
        "description": "Ticket Description",
        "author": "Author Name"
    }
    ```
- ### Error Response
  **Code** : `400 BAD REQUEST`  
  **Condition** : If ID is invalid (e.g., Format Error).
  ```json
    {
        "error": "Invalid ticket ID."
    }
  ```
  **Code** : `404 NOT FOUND`  
  **Condition** : If ID does not exist.
  ```json
    {
        "error": "Ticket not found."
    }
  ```

## 4. Update a Ticket
- **Method** : `PUT`
- **URL** : `/tickets/{id}`
- **Description**: Updates an entire ticket with new data.
- **Request Headers**:
    - `Content-Type`: `application/json`
- **Request Body (application/json)**:
    ```json
    {
        "title": "Updated Title",
        "description": "Updated Description",
      "author": "Updated Author"
    }
    ```
- ### Success Response
  **Code** : `200 OK`
    ```json
    {
        "title": "Updated Title",
        "description": "Updated Description",
        "author": "Updated Author"
    }
    ```
- ### Error Response
  **Code** : `400 BAD REQUEST`  
  **Condition** : Invalid or incomplete data.
  ```json
    {
        "error": "Invalid data for updating the ticket."
    }
  ```
  **Code** : `404 NOT FOUND`  
  **Condition** : If ID does not exist.
  ```json
    {
        "error": "Ticket not found."
    }
  ```
  
## 5. Delete a Ticket
- **Method** : `DELETE`
- **URL** : `/tickets/{id}`
- **Description**: Deletes a specific ticket by ID.
- ### Success Response
  **Code** : `204 NO CONTENT`
    ```json
    {
        "title": "Updated Title",
        "description": "Updated Description",
        "author": "Updated Author"
    }
    ```
- ### Error Response
  **Code** : `400 BAD REQUEST`  
  **Condition** : Invalid or incomplete data.
  ```json
    {
        "error": "Invalid data for updating the ticket."
    }
  ```
  **Code** : `404 NOT FOUND`  
  **Condition** : If ID does not exist.
  ```json
    {
        "error": "Ticket not found."
    }
  ```

## 6. Partial Update a Ticket (PATCH)
- **Method** : `PATCH`
- **URL** : `/tickets/{id}`
- **Description**: Partially updates an existing ticket.
- **Request Headers**:
  - `Content-Type`: `application/json`
- **Request Body (application/json) - _Optional fields for partial update_**:
    ```json
    {
        "title": "Partially updated Title",
        "description": "Partially updated Description",
        "author": "Partially updated Author"
    }
    ```
- ### Success Response
  **Code** : `200 OK`
    ```json
    {
        "title": "Updated Title",
        "description": "Updated Description",
        "author": "Updated Author"
    }
    ```
- ### Error Response
  **Code** : `400 BAD REQUEST`  
  **Condition** : Invalid or incomplete data.
  ```json
    {
        "error": "Invalid data for partial ticket update."
    }
  ```
  **Code** : `404 NOT FOUND`  
  **Condition** : If ID does not exist.
  ```json
    {
        "error": "Ticket not found."
    }
  ```

## Response Codes Summary
- `200 OK` : The request was successful.
- `201 Created` : A new resource was successfully created.
- `204 No Content` : The request was successful, but there is no content to return (e.g., DELETE).
- `400 Bad Request` : The request was invalid or malformed.
- `404 Not Found` : The requested resource does not exist.
- `415 Unsupported Media Type` : The request content type is unsupported.