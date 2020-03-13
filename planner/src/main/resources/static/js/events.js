$(document).ready(function () {
    $(document).on('click', '.sidebar-nav a', function () {
        $("#events").addClass('active');
        $('#invites').removeClass('active');
    });

    $("#createEvent").click(function () {
        $('#events-modal').toggle();
        $('#color').toggle();
    });

    let $events = [];
    $.ajax({
        url: 'http://localhost:8080/api/getCurrentUserEvents',
        type: 'GET',
        contentType: "application/json",
        success: function (data) {
            console.log(data);
            //Iterate through user's events
            // var events = [];
            for (var i = 0; i < data.length; i++) {
                console.log("id: " + data[i].id + ",");
                console.log("title: " + data[i].title + ",");
                console.log("start: " + data[i].start + ",");
                console.log("end: " + data[i].end + ",");
                console.log("allDay: " + data[i].allDay + ",");
                console.log("color: " + data[i].color + ",");
                console.log("tag: " + data[i].tag + ",");
                console.log("hostUsername: " + data[i].hostUsername + "e,");
                console.log("description: " + data[i].description);
                var event;
                if (data[i].end === null || data[i].end.length < 16) {
                    event = {
                        'id': data[i].id,
                        'title': data[i].title,
                        'allDay': data[i].allDay,
                        'start': new Date(data[i].start).toISOString(),
                        'end': '',
                        'color': data[i].color,
                        'tag': data[i].tag,
                        'hostUsername': data[i].hostUsername,
                        'description': data[i].description
                    };

                } else {
                    event = {
                        'id': data[i].id,
                        'title': data[i].title,
                        'allDay': data[i].allDay,
                        'start': new Date(data[i].start).toISOString(),
                        'end': new Date(data[i].end).toISOString(),
                        'color': data[i].color,
                        'tag': data[i].tag,
                        'hostUsername': data[i].hostUsername,
                        'description': data[i].description
                    };

                }
                console.log(event);
                $events.push(event);
                console.log($events);
                $('#eventCreateCalendar').fullCalendar('renderEvent', event, true);

                // $('#eventCreateCalendar').fullCalendar('renderEvent', event, true);
            }
        },
        error: function (e) {
            console.log(e);
            console.log(e.message);
        }
    });

    function formatDate(date) {
        // date.substring(11, 16);
        // date.substring(0, 10);

        // <div>
        //     <label class="item-heading">Date: &nbsp</label><label
        //         th:text="${(event.start).substring(0, 10)}"></label>
        // </div>
        // <div th:if="${event.allDay}">
        //     <label class="item-heading">Time: &nbsp</label><label><label
        //             th:text="${event.start.substring(11, 16)}"></label> -
        //         <label th:text="${event.end.substring(11, 16)}"></label></label>
        // </div>
        // <div th:if="${!event.allDay}">
        //     <label class="item-heading">All day event starting at: </label><label><label
        //             th:text="${event.start.substring(11, 16)}"></label>
        // </div>
        var monthNames = [
            "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December"
        ];

        var day = date.getDate();
        var monthIndex = date.getMonth();
        var year = date.getFullYear();

        return date.toISOString().substring(11, 16) + ' ' + day + ' ' + monthNames[monthIndex] + ' ' + year;
    }

    $("#createEvent").click(function () {
        $('#eventCreateCalendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            navLinks: true,
            editable: true,
            eventLimit: false,
            events: $events,
            eventClick: function (event, jsEvent, view) {
                $('#eventTitle').html('<span>' + event.title + '</span>');
                $('#eventStart').html('<span>' + formatDate(new Date(event.start)) + '</span>');
                if (event.end !== null && event.end.length > 0) {
                    $('#eventEnd').html('<span>' + formatDate(new Date(event.end)) + '</span>');
                }
                $('#eventType').html('<span>' + event.type + '</span>');
                $('#eventDescription').html('<span>' + event.description + '</span>');
                $('#eventDetailsModal').css('display: block;');
                if (event.allDay === true) {
                    $('#allDayCheckbox').prop("checked", true);;
                } else {
                    $('#allDayCheckbox').prop("checked", false);;
                }

                var modal = document.getElementById("eventDetailsModal");
                modal.style.display = "block";

                var span = document.getElementsByClassName("close")[0];
                span.onclick = function () {
                    modal.style.display = "none";
                }

                window.onclick = function (event) {
                    if (event.target == modal) {
                        modal.style.display = "none";
                    }
                }
                return false;
            }

        });

    });


    $('#color-picker').colorpicker({
        format: null,
        customClass: 'custom-colorpicker',
        sliders: {
            saturation: {
                maxLeft: 200,
                maxTop: 200
            },
            hue: {
                maxTop: 200
            },
            alpha: {
                maxTop: 200
            }
        }
    }).on('changeColor', function (value) {
        $('#color-picker').css('background-color', value.color.toHex());
        $("#color").prop("value", value.color.toHex());
        $('#tag-element').css('background-color', value.color.toHex());
    });

    $('#close-create').click(function () {
        $('#events-modal').toggle();
    });

    $('#close-modal').click(function () {
        $('#events-modal').toggle();
    });

    $('#allDay').click(function () {
        $('#end').attr('disabled', !$(this).attr('checked'));
    });

    //TODO : fix auto complete not updating while typing

    let $users = [];
    $.ajax({
        url: 'http://localhost:8080/api/allUsersList',
        type: 'GET',
        contentType: "application/json",
        success: function (data) {
            console.log(data);
            //Iterate through user's events
            // var events = [];
            for (var i = 0; i < data.length; i++) {
                // console.log("first name: " + data[i].firstName + ",");
                // console.log("Last name: " + data[i].lastName + ",");
                // console.log("email: " + data[i].email + ",");

                var user = data[i].firstName + ' ' + data[i].lastName + ' (' + data[i].email + ')';

                // console.log(user);
                $users.push(user);
                // console.log($users);
                // $('#eventCreateCalendar').fullCalendar('renderEvent', event, true);
            }
        },
        error: function (e) {
            console.log(e);
            console.log(e.message);
        }
    });

    $("#members").autocomplete({
        source: $users,
        select: function (event, ui) {
            if (ui.item.label) {
                //Send the user to be stored
                let $data = {};
                $data["username"] = ui.item.label;
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/api/addUserToEvent",
                    data: JSON.stringify($data),
                    dataType: 'json',
                    success: function (data) {
                        console.log("EMAIL: ");
                        console.log(data.email);
                        //Get the events based on given user to add from calendar
                        $data["email"] = data.email;
                        $.ajax({
                            url: 'http://localhost:8080/api/getUserEventsByEmail',
                            type: 'POST',
                            contentType: "application/json",
                            data: JSON.stringify($data),
                            success: function (data) {
                                console.log(data);
                                //Iterate through user's events
                                for (var i = 0; i < data.length; i++) {
                                    console.log("id: " + data[i].id + ",");
                                    console.log("title: " + data[i].title + ",");
                                    console.log("start: " + data[i].start + ",");
                                    console.log("end: " + data[i].end + ",");
                                    console.log("allDay: " + data[i].allDay + ",");
                                    console.log("color: " + data[i].color + ",");
                                    console.log("tag: " + data[i].tag + ",");
                                    console.log("hostUsername: " + data[i].hostUsername + "e,");
                                    console.log("description: " + data[i].description);
                                    var event = {
                                        'id': data[i].id,
                                        'title': data[i].title,
                                        'allDay': data[i].allDay,
                                        'start': new Date(data[i].start).toISOString(),
                                        'end': new Date(data[i].end).toISOString(),
                                        'color': data[i].color,
                                        'tag': data[i].tag,
                                        'hostUsername': data[i].hostUsername,
                                        'description': data[i].description
                                    };
                                    console.log(event);
                                    $events.push[event];
                                    $('#eventCreateCalendar').fullCalendar('renderEvent', event, true);
                                }
                            },
                            error: function (e) {
                                console.log(e);
                                console.log(e.message);
                            }
                        });
                    },
                    error: function (e) {
                        console.log(e);
                        console.log(e.message);
                    }
                });
                // var index = $users.findIndex(o => o === ui.item.label);
                // var removed = index!== -1 && $events.splice(index, 1);
                // console.log(removed);
                // console.log($users);
                //Add user to display
                $("<span/>", {
                    text: ui.item.label,
                    appendTo: "#invitedUsers",
                    class: "dashfolio-tag",
                    id: "tag-element",
                }).bind('click', function () {
                    console.log("REMOVING User: " + $(this).html());
                    let $data = {};
                    $data["username"] = $(this).html();
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "http://localhost:8080/api/deleteUserFromEvent",
                        data: JSON.stringify($data),
                        dataType: 'json',
                        success: function (data) {
                            console.log("EMAIL: ");
                            console.log(data.email);
                            //Get the events based on given user to remove from calendar
                            $data["email"] = data.email;
                            $.ajax({
                                url: 'http://localhost:8080/api/getUserEventsByEmail',
                                type: 'POST',
                                contentType: "application/json",
                                data: JSON.stringify($data),
                                success: function (data) {
                                    console.log(data);
                                    for (var i = 0; i < data.length; i++) {
                                        console.log("id: " + data[i].id + ",");
                                        var index = $events.findIndex(o => o.id === data[id]);
                                        var removed = index !== -1 && $events.splice(index, 1);
                                        console.log(removed);
                                        console.log($events);
                                        $('#eventCreateCalendar').fullCalendar('removeEvents', [data[i].id]);
                                    }
                                },
                                error: function (e) {
                                    console.log(e);
                                    console.log(e.message);
                                }
                            });
                        },
                        error: function (e) {
                            //called when there is an error
                            console.log(e);
                            console.log(e.message);
                        }
                    });
                });
                this.value = "";
            }
        },
        close: function (el) {
            el.target.value = '';
        }
    });

    $(".deleteEvent").click(function () {
        $(this).find('input').each(function () {
            let $data = {};
            $data["id"] = $(this).val();
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/deleteEvent",
                data: JSON.stringify($data),
                dataType: 'json'
            });
        });
    });

    $(".updateEvent").click(function () {
        $(this).find('input').each(function () {
            console.log($(this).val());
        });
    });
});