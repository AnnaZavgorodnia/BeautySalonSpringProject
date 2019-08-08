const time = ["09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00","16:00","17:00"];

const MASTER_ID = window.location.pathname.split('/')[2];

document.addEventListener('DOMContentLoaded', function() {

    let masterImg = document.createElement("img");
    masterImg.setAttribute("src",`/static/images/masters/${MASTER_ID}.jpg`);
    document.getElementById("masterImg").appendChild(masterImg);

    getServices(MASTER_ID)
        .then(data => {
            renderServices(data);
            let elems = document.querySelectorAll('select');
            let instances = M.FormSelect.init(elems,{});
        })
        .catch(e => console.log(e));

    document.getElementById("select-service").addEventListener("change",function(e){
        console.log(e.target);
        let inp = document.getElementById('select-service').M_FormSelect.input.value;
        console.log(inp);
        let val = document.getElementById(inp);
        document.getElementById("price").innerHTML = "Price: " + val.dataset.price;
    });

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


});

function renderServices(data){

    let select = document.getElementById("select-service");

    data.forEach(el => {
        let option = document.createElement("option");
        option.setAttribute("value", el.name);
        option.setAttribute("id", el.name);
        option.dataset.price = el.price;
        option.innerHTML = el.name;
        select.appendChild(option);
    });

    let first = select.firstElementChild;
    first.setAttribute("selected", true);
    document.getElementById("price").innerHTML = "Price: " + first.dataset.price;
}

async function getApps(id,date) {
    let response = await fetch(`http://localhost:8088/api/masters/${id}/appointments/${date}`);
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

    document.getElementById("app_date").innerHTML = "Date: " + date.toDateString();

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
    btn.addEventListener("click", makeApp);
    btn.innerHTML = "book";
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

function setTime(e){
    document.getElementById("time").innerHTML = "Time: " +
        e.target.dataset.time;
}

function makeApp(e){

    setTime(e);

    let someDate = document.getElementsByClassName('datepicker')[0];
    let instance = M.Datepicker.getInstance(someDate);
    let idate = instance.date;

    let finalDate = new Date(idate.getFullYear(),
        idate.getMonth(),
        idate.getDate(),
        e.target.dataset.time.substring(0,2));

    console.log(finalDate);
    finalDate.setHours(finalDate.getHours()+3);

    let serviceName = document.getElementById('select-service').M_FormSelect.input.value;

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
                serviceName: serviceName
            })
        });
        const content = await rawResponse.json();

        document.getElementById(content.appTime.substring(0,5)).setAttribute("class","btn-black btn-disabled");
        console.log(content);
    })();
}
