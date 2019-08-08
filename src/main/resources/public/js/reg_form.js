document.addEventListener('DOMContentLoaded', function() {

    document.getElementById("reg_form").addEventListener("submit", regUser);

});

function regUser(e) {
    e.preventDefault();

    addClient()
        .then(data => {
            if(data == null){
                document.getElementById("username-error-exists").innerHTML = "User with such username already exists";
            } else if(data.errors != null){
                data.errors.forEach(el => {
                   document.getElementById(`${el.field}-error`).innerHTML = el.defaultMessage;
                });
            }
        })
        .catch(e => console.log(e));
}

async function addClient() {

    let username = document.getElementById("username").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    let response = await fetch('http://localhost:8088/api/clients', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password
        })
    });

    if(response.status === 400) return null;

    return await response.json();
}