document.addEventListener('DOMContentLoaded', function() {

    getPositions()
        .then(data => {
            console.log(data);
            renderPositions(data);
            let elem = document.getElementById('position-select');
            let instances = M.FormSelect.init(elem, {});
        })
        .catch(e => console.log(e));

    getServices()
        .then(data => {
            console.log(data);
            renderServices(data);
            let elem = document.getElementById('service-select');
            let instances = M.FormSelect.init(elem, {});
        })
        .catch(e => console.log(e));

    let elems = document.querySelectorAll('select');
    let instances = M.FormSelect.init(elems, {});


    document.getElementById("add_master").addEventListener("submit", addMaster);

});


async function getServices(){
    let response = await fetch(`http://localhost:8088/api/services`);
    let data = await response.json();
    return data;
}

async function getPositions(){
    let response = await fetch(`http://localhost:8088/api/positions`);
    let data = await response.json();
    return data;
}


function renderPositions(data){
    let select = document.getElementById("position-select");

    data.forEach(el => {
        let option = document.createElement("option");
        option.setAttribute("value", el);
        option.innerHTML = el;
        select.appendChild(option);
    });

    let first = select.firstElementChild;
    first.setAttribute("selected", true);
}

function renderServices(data){
    let select = document.getElementById("service-select");

    data.forEach(el => {
        let option = document.createElement("option");
        option.setAttribute("value", el.id);
        option.innerHTML = el.name;
        select.appendChild(option);
    });

    let first = select.firstElementChild;
    first.setAttribute("selected", true);
}


function addMaster(e){

    e.preventDefault();

    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let email = document.getElementById("email").value;
    let instagram = document.getElementById("instagram").value;

    console.log(firstName);
    console.log(lastName);
    console.log(username);
    console.log(password);
    console.log(email);
    console.log(instagram);

    let position = document.getElementById('position-select').M_FormSelect.input.value;
    console.log(position);

    let instance = M.FormSelect.getInstance(document.getElementById("service-select"));
    let ar = instance.getSelectedValues();

    console.log(ar);

    (async () => {
        const rawResponse = await fetch('http://localhost:8088/api/masters', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                username: username,
                password: password,
                email: email,
                instagram: instagram,
                services: ar,
                position: position
            })
        });

            if(rawResponse.status === 409){
                document.getElementById("username-error").innerHTML = "Username already exists";
            } else{

                const content = await rawResponse.json();

                console.log(content);

                if(content.errors){
                    content.errors.forEach(el => {
                        document.getElementById(`${el.field}-error`).innerHTML = el.defaultMessage;
                    });
                }

            }


        //
        // document.getElementById(content.appTime.substring(0,5)).setAttribute("class","btn-black btn-disabled");
        // console.log(content);
    })();


    // let form = new FormData();
    // form.append("file",document.getElementById("image").files[0]);
    //
    // (async () => {
    //     const rawResponse = await fetch('http://localhost:8088/api/image', {
    //         method: 'POST',
    //         enctype: 'multipart/form-data',
    //         data: form,
    //         dataType: 'json'
    //     });
    //     // const content = await rawResponse.json();
    //     //
    //     // document.getElementById(content.appTime.substring(0,5)).setAttribute("class","btn-black btn-disabled");
    //     // console.log(content);
    // })();
}

