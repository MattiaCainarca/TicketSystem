const ticketDetails = document.getElementById("ticketDetails")

function removeDetails() {
    ticketDetails.innerHTML = "";
}

function showTicketDetails(id) {
    fetch(`/ticket/getTicket/` + id)
        .then(response => {
            if (!response.ok) throw new Error("Error fetching tickets ", response.status, response.statusText);
            return response.json();
        })
        .then(ticket => {
            ticketDetails.innerHTML = `
                <div class="text-start px-4">
                   <button id="removeDetails" type="button" class="btn btn-primary my-3" onclick="removeDetails()">Remove Details</button>
                   <p>Title: ${ticket.title}</p>
                   <hr class="my-1">
                   <p>Type: ${ticket.type}</p>
                   <hr class="my-1">
                   <p>Author: ${ticket.user.username}</p>
                   <hr class="my-1">
                   <p>Assignee: ${ticket.userAssignee.username}</p>
                   <hr class="my-1">
                   <p>Time Estimate: ${ticket.timeEstimate}</p>
                   <hr class="my-1">
                   <p>Time Spent: ${ticket.timeSpent}</p>
                   <hr class="my-1">
                   <p>Created Date: <time>${new Date(ticket.createdDate).toLocaleDateString()}</time></p>
                   <hr class="my-1">
                   <p>Due Date: <time>${new Date(ticket.dueDate).toLocaleDateString()}</time></p>
                   <hr class="my-1">
                   <p>Status: ${ticket.status}</p>
                </div>`;
        })
        .catch(error => console.error("Error: ", error));
}