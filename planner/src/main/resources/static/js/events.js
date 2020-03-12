$(document).ready(function () {
    $(document).on('click', '.sidebar-nav a', function () {
        $("#events").addClass('active');
        $('#invites').removeClass('active');
    });

    $("#createEvent").click(function () {
        $('#events-modal').toggle();
        $('#color').toggle();
    });

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
            events: {
                url: '/api/event/all'
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

    $("#members").autocomplete({
        source: 'http://localhost:8080/api/allUsersList',
        select: function (event, ui) {
            if (ui.item.label) {
                //Send the user to be stored
                // 'http://localhost:8080/api/event/addUser?username=' + ui.item.label;
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
                        //called when there is an error
                        console.log(e);
                        console.log(e.message);
                    }
                });
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

                //Add the user to the event and get their calendar
                //TODO : get all the events of the added user and add them
                $('#eventCreateCalendar').fullCalendar('renderEvent', {
                    id: 5,
                    title: 'Some Title',
                    start: '2020-03-30',
                    end: '2020-03-30',
                    className: 'fancy-color'
                }, true);
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