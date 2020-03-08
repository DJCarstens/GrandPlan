$(document).ready(function () {
    $(document).on('click', '.sidebar-nav a', function () {
        $("#events").addClass('active');
        $('#invites').removeClass('active');
    });

    $("#createEvent").click(function () {
        $('#events-modal').css('display', 'block');
        $('#color').css("display", "none");
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
            eventLimit: true,
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
        $('#events-modal').css('display', 'none');
    });

    $('#close-modal').click(function () {
        $('#events-modal').css('display', 'none');
    });

    $('#allDay').click(function () {
        $('#end').attr('disabled', !$(this).attr('checked'));
    });

    //Get users to add to event
    var tags = [
        "Delhi",
        "Ahemdabad",
        "Punjab",
        "Uttar Pradesh",
        "Himachal Pradesh",
        "Karnatka",
        "Kerela",
        "Maharashtra",
        "Gujrat",
        "Rajasthan",
        "Bihar",
        "Tamil Nadu",
        "Haryana"];
    console.log("TAGS");
    console.log(tags);

    //TODO: replace "tags" with userlist

    $("#members").autocomplete({
        source: tags,
        select: function (event, ui) {
            if (ui.item.label) {
                $("<span/>", {
                    text: ui.item.label,
                    appendTo: "#invitedUsers",
                    class: "dashfolio-tag",
                    id: "tag-element",
                }).bind('click', function () {
                    console.log("REMOVING User: " + $(this).html());
                    //Remove that user's events from the calendar
                });
                //Add the user to the event and get their calendar
                this.value = "";
            }
        },
        close: function (el) {
            el.target.value = '';
        }
    });
    
    $(".deleteEvent").click(function () {
        $(this).find('input').each(function(){
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

    $(".updateEvent").click(function(){
        $(this).find('input').each(function(){
            console.log($(this).val());
        });
    });
});