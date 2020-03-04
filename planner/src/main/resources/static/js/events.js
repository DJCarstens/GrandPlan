$(document).ready(function () {
    $(document).on('click', '.sidebar-nav a', function () {
        $("#events").addClass('active');
        $('#invites').removeClass('active');
    });

    $("#createEvent").click(function () {
        $('#events-modal').css('display', 'block');
    });
    $("#createEvent").click(function() {
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
                    url : '/api/event/all'
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
});