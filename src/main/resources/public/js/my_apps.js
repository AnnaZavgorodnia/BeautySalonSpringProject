let CURRENT_PAGE = 0;
const ELEMENTS_PER_PAGE = 3;

document.addEventListener('DOMContentLoaded', function() {

    document.getElementById("load-button").addEventListener("click", loadMore);

    loadMore();

});

function removeButton(){
    document.getElementById("load-button").remove();
}

function loadMore(){
    getApps(CURRENT_PAGE, ELEMENTS_PER_PAGE)
        .then(data => {
            fillApps(data.content);
            CURRENT_PAGE++;
            if(CURRENT_PAGE >= data.totalPages){
                removeButton();
            }
        })
        .catch(e => console.log(e));
}


async function getApps(page,size) {
    let response = await fetch(`http://localhost:8088/api/me/appointments?page=${page}&size=${size}`);
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

       let serviceName = document.createElement("p");
       serviceName.setAttribute("class","card-text");

       let price = document.createElement("p");
       price.setAttribute("class","card-text");

       if(MY_APP.locale === "en"){
           master.innerHTML = MY_APP.messages.master + ": " + el.master.fullName;
           serviceName.innerHTML = MY_APP.messages.service + ":" + el.service.name;
           price.innerHTML = MY_APP.messages.price + ":" + el.service.price;
       } else{
           master.innerHTML = MY_APP.messages.master + ": " + el.master.fullNameUa;
           serviceName.innerHTML = MY_APP.messages.service + ": " + el.service.nameUa;
           price.innerHTML = MY_APP.messages.price + ": " + el.service.price;
       }

       body.appendChild(master);
       body.appendChild(serviceName);
       body.appendChild(price);

       let btn = document.createElement("button");
       btn.setAttribute("type","button");
       btn.setAttribute("class","btn btn-danger");
       btn.dataset.appId = el.id;
       btn.innerHTML = MY_APP.messages.cancel;
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