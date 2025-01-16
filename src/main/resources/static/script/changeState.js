const state = document.getElementById("state");

function changeState(id) {
    fetch(`/ticket/` + id + `/changeState`)
        .then(response => {
            if (!response.ok) throw new Error("Error fetching tickets ", response.status, response.statusText);
            return response.json();
        })
        .then(status => {
            state.innerHTML = status;
        })
        .catch(error => console.error("Error: ", error));
}