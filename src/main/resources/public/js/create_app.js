const time = ["09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00","16:00","17:00"];

const MASTER_ID = window.location.pathname.split('/')[2];

document.addEventListener('DOMContentLoaded', function() {


    getMaster(MASTER_ID)
        .then(master => {
            let masterImg = document.createElement("img");
            masterImg.setAttribute("src",master.imagePath);
            document.getElementById("masterImg").appendChild(masterImg);

            let image = document.createElement("img");
            image.setAttribute("src",master.imagePath);
            image.setAttribute("id","img");
            image.style.width = "60%";
            document.getElementById("master_image").appendChild(image);
            if(MY_APP.locale === "en")
                document.getElementById("master-modal").innerHTML = master.fullName;
            else
                document.getElementById("master-modal").innerHTML = master.fullNameUa;
        })
        .catch(e => console.log(e));

    let elems_modal = document.querySelectorAll('.modal');
    let instances_modal = M.Modal.init(elems_modal,{startingTop: "4%",endingTop: "30%"});

    getServices(MASTER_ID)
        .then(data => {
            renderServices(data);
            let elems = document.querySelectorAll('select');
            let instances = M.FormSelect.init(elems,{});
            document.getElementById("select-service").addEventListener("change",function(e){
                console.log(e.target);
                let inp = document.getElementById('select-service').M_FormSelect.input.value;
                console.log(inp);
                document.getElementById("service-modal").innerHTML = inp;
                let val = document.getElementById(inp);
                document.getElementById("price-modal").innerHTML = val.dataset.price + "UAH";
                document.getElementById("select_label").style.color = "black";
            });
        })
        .catch(e => console.log(e));



    Date.prototype.addDays = function(days) {
        let date = new Date(this.valueOf());
        date.setDate(date.getDate() + days);
        return date;
    };

    let date = new Date();

    M.Datepicker.init(document.getElementsByClassName('datepicker')[0],
    {
        disableWeekends: true,
        firstDay: 1,
        minDate: new Date(),
        maxDate: date.addDays(14),
        onClose: ondraw
    });

    document.getElementById("button-book").addEventListener("click", makeApp);

});

function renderServices(data){

    let select = document.getElementById("select-service");

    data.forEach(el => {
        let option = document.createElement("option");
        if(MY_APP.locale === "en"){
            option.setAttribute("id", el.name);
            option.innerHTML = el.name;
        } else{
            option.setAttribute("id", el.nameUa);
            option.innerHTML = el.nameUa;
        }
        option.setAttribute("value", el.name);
        option.dataset.price = el.price;
        select.appendChild(option);
    });
}

async function getApps(id,date) {
    let response = await fetch(`http://localhost:8088/api/masters/${id}/appointments/${date}`);
    let data = await response.json();
    return data;
}

async function getMaster(id) {
    let response = await fetch(`http://localhost:8088/api/masters/${id}`);
    let data = await response.json();
    return data;
}

async function getServices(id) {
    let response = await fetch(`http://localhost:8088/api/masters/${id}/services`);
    let data = await response.json();
    return data;
}

const ondraw = async function fullTime(){

    clearTime();

    let instance = M.Datepicker.getInstance(document.getElementsByClassName('datepicker')[0]);

    let date = new Date(instance.date);
    date.setHours(date.getHours()+3);

    const data = await getApps(MASTER_ID, date.toISOString());
    console.log(data);

    time.forEach(el => {
        createSchedule(el, data);
    });

};

function clearTime(){
    document.getElementById("time-table").innerHTML = "";
}

function createSchedule(time, data){
    let timeRow = document.createElement("div");
    timeRow.classList = "time__row";
    let title = document.createElement("h4");
    title.innerHTML = time;
    let btn = document.createElement("button");
    btn.addEventListener("click", fullModal);
    btn.innerHTML = MY_APP.messages.book;
    btn.setAttribute("class","btn-black btn-active");
    btn.setAttribute("id",time);
    btn.dataset.time = time;
    console.log("time: " + time);
    data.forEach(el => {
        datetext = el.appTime.substring(0,5);
        console.log("datetext: " + datetext);
        if(datetext === time){
            console.log(true);
            btn.setAttribute("disabled",true);
            btn.setAttribute("class","btn-black btn-disabled");
        }
    });
    timeRow.appendChild(title);
    timeRow.appendChild(btn);
    let timeTable = document.getElementById("time-table");
    timeTable.appendChild(timeRow);
}

function fullModal(e) {
    let serviceName = document.getElementById('select-service').M_FormSelect.input.value;
    let serviceElement = document.getElementById(serviceName);
    if(serviceElement == null){
        console.log(true);
        document.getElementById("select_label").style.color = "red";

    } else{

        let someDate = document.getElementsByClassName('datepicker')[0];
        let instance = M.Datepicker.getInstance(someDate);
        let idate = instance.date;

        let finalDate = new Date(idate.getFullYear(),
            idate.getMonth(),
            idate.getDate(),
            e.target.dataset.time.substring(0,2));
        console.log(finalDate);
        finalDate.setHours(finalDate.getHours()+3);
        let appDate = finalDate.toISOString().substring(0,10);
        let appTime = finalDate.toISOString().substring(11,19);

        let date_el = document.getElementById("date-modal");
        let time_el = document.getElementById("time-modal");
        date_el.value = appDate;
        time_el.value = appTime;
        date_el.innerHTML = appDate;
        time_el.innerHTML = appTime.substring(0,5);

        var inst = M.Modal.getInstance(document.getElementById("modal"));
        inst.open();
    }
}

function makeApp(){

    let someDate = document.getElementsByClassName('datepicker')[0];
    let instance = M.Datepicker.getInstance(someDate);
    let idate = instance.date;

    let time = document.getElementById("time-modal").value;

    let finalDate = new Date(idate.getFullYear(),
        idate.getMonth(),
        idate.getDate(),
        time.substring(0,2));

    console.log(finalDate);
    finalDate.setHours(finalDate.getHours()+3);

    let serviceName = document.getElementById('select-service').M_FormSelect.input.value;
    let option = document.getElementById(serviceName).value;

    (async () => {
        const rawResponse = await fetch('http://localhost:8088/api/appointments', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                master: MASTER_ID,
                appDate: finalDate.toISOString(),
                serviceName: option
            })
        });
        const content = await rawResponse.json();

        document.getElementById(content.appTime.substring(0,5)).setAttribute("class","btn-black btn-disabled");
        console.log(content);
    })();
}
