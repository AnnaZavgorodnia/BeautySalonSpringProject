document.addEventListener('DOMContentLoaded', function() {

        getMasters()
            .then(data => {
                console.log(data);
                renderMastersList(data);
            })
            .catch(e => console.log(e));
});

function renderMastersList(data){

    let list = document.getElementById("collection");

    data.forEach(el =>{
        let li = document.createElement("li");
        li.setAttribute("class","collection-item avatar");
        let img = document.createElement("img");
        img.setAttribute("src",el.imagePath);
        img.setAttribute("class", "circle");
        let span = document.createElement("span");
        span.setAttribute("class","title");
        span.innerHTML = el.fullName;
        let p = document.createElement("p");
        p.innerHTML = el.position;
        let a = document.createElement("a");
        a.setAttribute("href","#");
        a.setAttribute("class","secondary-content");
        let i = document.createElement("i");
        i.setAttribute("class","fas fa-user-edit");
        a.appendChild(i);
        li.appendChild(img);
        li.appendChild(span);
        li.appendChild(p);
        li.appendChild(a);
        list.appendChild(li);
    });
}

async function getMasters() {
    let response = await fetch(`http://localhost:8088/api/masters`);
    let data = await response.json();
    return data;
}