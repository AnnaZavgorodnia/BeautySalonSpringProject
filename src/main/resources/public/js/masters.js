document.addEventListener('DOMContentLoaded', function() {

    getMasters()
        .then(data => {
            console.log(data);
            renderMasters(data);
        })
        .catch(e => console.log(e));
});

function renderMasters(data){
    let masters = document.getElementById("masters");
    data.forEach(m => {
        console.log(m);
        let about_item = document.createElement("div");
        about_item.className = "about__item";
        let about_image = document.createElement("div");
        about_image.className = "about__img";
        let master_image = document.createElement("img");
        master_image.setAttribute("src", m.imagePath);
        about_image.appendChild(master_image);
        about_item.appendChild(about_image);
        let about_text = document.createElement("div");
        about_text.className = "about__text";
        let name = document.createElement("h4");
        if(MY_APP.locale === "en"){
            name.innerHTML = m.fullName;
        } else {
            name.innerHTML = m.fullNameUa;
        }
        let inst_a = document.createElement("a");
        inst_a.setAttribute("href",`https://www.instagram.com/${m.instagram}/`);
        const icon = document.createElement("i");
        icon.setAttribute("class","fab fa-instagram");
        inst_a.appendChild(icon);
        let make_app_a = document.createElement("a");
        make_app_a.setAttribute("href",`/create_app/${m.id}`);
        make_app_a.setAttribute("class","btn app-btn");
        make_app_a.text = MY_APP.messages.make_an_app;
        about_text.appendChild(name);
        about_text.appendChild(inst_a);
        about_text.appendChild(make_app_a);
        about_item.appendChild(about_text);
        masters.appendChild(about_item);
    });
}

async function getMasters() {
    let response = await fetch(`http://localhost:8088/api/masters`);
    let data = await response.json();
    return data;
}