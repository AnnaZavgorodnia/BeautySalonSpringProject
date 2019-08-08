document.addEventListener('DOMContentLoaded', function() {

    getApps()
        .then(data => fillApps(data))
        .catch(e => console.log(e));
});

async function getApps() {
    let response = await fetch("http://localhost:8088/api/me/appointments");
    let data = await response.json();
    console.log(data);
    return data;
}

function fillApps(data) {
    data.forEach(el => {
       let card = document.createElement("div");
       card.setAttribute("class","card my-card");
       card.setAttribute("id",el.id);
       let header = document.createElement("div");
       header.setAttribute("class","card-header");
       let title = document.createElement("h5");
       title.innerHTML = el.appDate + " " + el.appTime;
       header.appendChild(title);
       card.appendChild(header);

       let body = document.createElement("div");
       body.setAttribute("class","card-body");

       let master = document.createElement("p");
       master.setAttribute("class","card-text");
       master.innerHTML = "Master: " + el.master.fullName;
       body.appendChild(master);

       let serviceName = document.createElement("p");
       serviceName.setAttribute("class","card-text");
       serviceName.innerHTML = "Service: " + el.service.name;
       body.appendChild(serviceName);

       let price = document.createElement("p");
       price.setAttribute("class","card-text");
       price.innerHTML = "Price: " + el.service.price;
       body.appendChild(price);

       let btn = document.createElement("button");
       btn.setAttribute("type","button");
       btn.setAttribute("class","btn btn-danger");
       btn.dataset.appId = el.id;
       btn.innerHTML = "Cancel";
       btn.addEventListener("click",cancelApp);
       body.appendChild(btn);
       card.appendChild(body);
       document.getElementsByClassName("user__app__content")[0].appendChild(card);

    });
}

function cancelApp(e){

    let appId = e.target.dataset.appId;

    console.log(appId);

    (async () => {
        const rawResponse = await fetch(`http://localhost:8088/api/appointments/${appId}`, {
            method: 'DELETE'
        });
        console.log(rawResponse);
        if(rawResponse.status === 200)
            document.getElementById(appId).remove();
    })();
}