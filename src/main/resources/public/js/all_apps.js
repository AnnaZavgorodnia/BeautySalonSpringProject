document.addEventListener('DOMContentLoaded', function() {

    console.log(MY_APP.contextPath);
    console.log(MY_APP.user);

    var calendarEl = document.getElementById('calendar');

    (async () => {
        const rawResponse =
            MY_APP.user.role === "ADMIN" ? await fetch('http://localhost:8088/api/appointments')
                                        : await fetch(`http://localhost:8088/api/masters/${MY_APP.user.id}/appointments`);
        const content = await rawResponse.json();

        const ar = content.map(el => {
            let bla = {};
            bla.title = "Master: " + el.master.fullName +
                        "\nClient: " + el.client.fullName;
            let date = new Date(el.appDate);
            date.setHours(Number(el.appTime.substring(0,2))+3);
            bla.start = date.toISOString();
            return bla;
        });

        console.log(ar);

        console.log(content);

        var calendar = new FullCalendar.Calendar(calendarEl, {
            plugins: [ 'timeGrid','dayGrid' ],
            height: 575,
            header: {
                left: 'dayGridMonth,timeGridWeek,timeGridDay custom1',
                center: 'title',
                right: 'custom2 prevYear,prev,next,nextYear'
            },
            minTime: "09:00:00",
            maxTime: "18:00:00",
            weekends: false,
            events: ar,
            navLinks: true
        });

        calendar.render();
    })();

});