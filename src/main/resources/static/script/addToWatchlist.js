const numWatched = document.getElementById("numWatched");

document.addEventListener("DOMContentLoaded", showNumTicketsWatched);

function showNumTicketsWatched() {
    if (numWatched)
        fetch(`/ticket/numTicketsWatched`)
            .then(response => {
                if (!response.ok) throw new Error("Error fetching tickets ", response.status, response.statusText);
                return response.json();
            })
            .then(num => {
                numWatched.innerHTML = "Watched: " + num;
            })
            .catch(error => console.error("Error: ", error));
}

function addToWatchlist(id) {
    fetch(`/ticket/watchlist/` + id)
        .then(response => {
            if (!response.ok) throw new Error("Error fetching tickets ", response.status, response.statusText);
            return response.json();
        })
        .then(flag => {
            let splitString = numWatched.textContent.split(' ')
            splitString[1] = parseInt(splitString[1], 10) + 1
            numWatched.innerHTML = splitString[0] + " " + splitString[1]
        })
        .catch(error => console.error("Error: ", error));
}
