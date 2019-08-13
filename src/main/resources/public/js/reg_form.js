document.addEventListener('DOMContentLoaded', function() {

    document.getElementById("reg_form").addEventListener("submit", regUser);

});

function regUser(e) {
    e.preventDefault();

    addClient()
        .then(data => {
            console.log(data);
            if(data == null){
                document.getElementById("username-error").innerHTML = MY_APP.messages.error_exists;
            } else if(data.errors){
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


    console.log(response);

    clearErrors();

    if(response.status === 409) return null;

    return await response.json();
}

function clearErrors() {
    document.getElementById("username-error").innerHTML = "";
    document.getElementById("firstName-error").innerHTML = "";
    document.getElementById("lastName-error").innerHTML = "";
    document.getElementById("email-error").innerHTML = "";
    document.getElementById("password-error").innerHTML = "";
}