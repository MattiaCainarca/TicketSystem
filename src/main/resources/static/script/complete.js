const completeBtn = document.getElementById("completedBtn");
const state = document.getElementById("state");

function completed(id) {
    fetch(`/milestone/` + id + `/completed`)
        .then(response => {
            if (!response.ok) throw new Error("Error fetching tickets ", response.status, response.statusText);
            return response.json();
        })
        .then(flag => {
            completeBtn.remove();
            state.innerHTML = "Completed";
        })
        .catch(error => console.error("Error: ", error));
}