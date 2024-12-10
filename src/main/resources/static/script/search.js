function getStatusClass(status) {
    switch (status) {
        case 'OPEN':
            return 'bg-danger';
        case 'IN_PROGRESS':
            return 'bg-warning';
        case 'DONE':
            return 'bg-success';
        case 'CLOSED':
            return 'bg-primary';
        default:
            return 'bg-secondary';
    }
}

function toggleLoading(show) {
    const loading = document.getElementById("loading");
    loading.style.display = show ? "block" : "none";
}

function searchTickets(query) {
    const container = document.querySelector(".row-cols-1");

    toggleLoading(true);

    if (query === "") {
        fetch(`/tickets`)
            .then(response => {
                if (!response.ok) throw new Error("Error fetching all tickets");
                return response.json();
            })
            .then(tickets => {
                container.innerHTML = "";
                if (tickets.length === 0) {
                    container.innerHTML = '<p class="fs-4 fst-italic">No tickets available.</p>';
                    return;
                }
                tickets.forEach(ticket => {
                    const card = `
                        <div class="col">
                            <article class="card text-bg-dark">
                                <div class="card-header ${getStatusClass(ticket.status)}">
                                    <strong>${ticket.title}</strong>
                                </div>
                                <div class="card-body">
                                    <p><strong>Creation Date: </strong>${new Date(ticket.createdDate).toLocaleDateString()}</p>
                                    <p><strong>Status: </strong>${ticket.status}</p>
                                    <p class="text-truncate"><strong>Description: </strong>${ticket.description}</p>
                                    <a href="/ticket/${ticket.id}" class="btn btn-primary btn-sm">Open Detail</a>
                                </div>
                            </article>
                        </div>
                    `;
                    container.innerHTML += card;
                });
            })
            .catch(error => console.error("Error:", error))
            .finally(() => toggleLoading(false));
        return;
    }

    if (query.length < 3) {
        toggleLoading(false);
        container.innerHTML = '<p class="fs-4 text-center fst-italic">Type at least 3 characters to search.</p>';
    }else {

        fetch(`/tickets/search?q=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) throw new Error("Error fetching tickets");
                return response.json();
            })
            .then(tickets => {
                container.innerHTML = "";

                if (tickets.length === 0) {
                    container.innerHTML = '<p class="fs-4 fst-italic">No tickets found.</p>';
                    return;
                }

                tickets.forEach(ticket => {
                    const card = `
                    <div class="col">
                        <article class="card text-bg-dark">
                            <div class="card-header ${getStatusClass(ticket.status)}">
                                <strong>${ticket.title}</strong>
                            </div>
                            <div class="card-body">
                                <p><strong>Creation Date: </strong>${new Date(ticket.createdDate).toLocaleDateString()}</p>
                                <p><strong>Status: </strong>${ticket.status}</p>
                                <p class="text-truncate"><strong>Description: </strong>${ticket.description}</p>
                                <a href="/ticket/${ticket.id}" class="btn btn-primary btn-sm">Open Detail</a>
                            </div>
                        </article>
                    </div>
                `;
                    container.innerHTML += card;
                });
            })
            .catch(error => console.error("Error:", error))
            .finally(() => toggleLoading(false));
    }
}

const searchInput = document.getElementById("search");
searchInput.addEventListener("input", function () {
    const query = searchInput.value.trim();
    searchTickets(query);
});
