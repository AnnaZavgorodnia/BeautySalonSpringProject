document.addEventListener('DOMContentLoaded', function() {

    console.log(MY_APP.user);

    (async () => {


        let rawResponse;

        if(MY_APP.user.role === "MASTER"){
            rawResponse = await fetch(`http://localhost:8088/api/masters/${MY_APP.user.id}`);
        } else{
            rawResponse = await fetch(`http://localhost:8088/api/users/${MY_APP.user.id}`);
        }

        const content = await rawResponse.json();

        if(MY_APP.user.role === "MASTER"){
            if(MY_APP.locale === "en"){
                document.getElementById("name").innerHTML = content.fullName;
            }else{
                document.getElementById("name").innerHTML = content.fullNameUa;
            }
            document.getElementById("email").innerHTML = content.email;
            document.getElementById("role").innerHTML = content.role;
            document.getElementById("image").setAttribute("src", content.imagePath);

        } else{
            document.getElementById("name").innerHTML = content.fullName;
            document.getElementById("email").innerHTML = content.email;
            document.getElementById("role").innerHTML = content.role;
        }
        console.log(content);

    })();

});