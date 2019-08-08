const time = ["09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00","16:00","17:00"];

const MASTER_ID = window.location.pathname.split('/')[2];

document.addEventListener('DOMContentLoaded', function() {

    let masterImg = document.createElement("img");
    masterImg.setAttribute("src",`/static/images/masters/${MASTER_ID}.jpg`);
    document.getElementById("masterImg").appendChild(masterImg);

    let image = document.createElement("img");
    image.setAttribute("src",`/static/images/masters/${MASTER_ID}.jpg`);
    image.setAttribute("id","img");
    image.style.width = "60%";
    document.getElementById("master_image").appendChild(image);

    var elems_modal = document.querySelectorAll('.modal');
    var instances_modal = M.Modal.init(elems_modal,{startingTop: "4%",endingTop: "30%"});

    getServices(MASTER_ID)
        .then(data => {
            renderServices(data);
            let elems = document.querySelectorAll('select');
            let instances = M.FormSelect.init(elems,{});
            document.getElementById("select-service").addEventListener("change",function(e){
                console.log(e.target);
                let inp = document.getElementById('select-service').M_FormSelect.input.value;
                console.log(inp);
                document.getElementById("service-modal").innerHTML = "Service: " + inp;
                let val = document.getElementById(inp);
                document.getElementById("price").innerHTML = "Price: " + val.dataset.price;
                document.getElementById("price-modal").innerHTML = "Price: " + val.dataset.price + "UAH";
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
        option.setAttribute("value", el.name);
        option.setAttribute("id", el.name);
        option.dataset.price = el.price;
        option.innerHTML = el.name;
        select.appendChild(option);
    });
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
    btn.addEventListener("click", fullModal);
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

function fullModal(e) {
    let serviceName = document.getElementById('select-service').M_FormSelect.input.value;
    let serviceElement = document.getElementById(serviceName);
    if(serviceElement == null){
        console.log(true);
        document.getElementById("select_label").style.color = "red";

    } else{
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
        let appDate = finalDate.toISOString().substring(0,10);
        let appTime = finalDate.toISOString().substring(11,19);

        let date_el = document.getElementById("date-modal");
        let time_el = document.getElementById("time-modal");
        date_el.value = appDate;
        time_el.value = appTime;
        date_el.innerHTML = "Date: " + appDate;
        time_el.innerHTML = "Time: " + appTime.substring(0,5);

        var inst = M.Modal.getInstance(document.getElementById("modal"));
        inst.open();
    }
}

function makeApp(e){

    setTime(e);

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
