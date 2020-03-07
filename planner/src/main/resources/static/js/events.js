$(document).ready(function () {
    $(document).on('click', '.sidebar-nav a', function () {
        $("#events").addClass('active');
        $('#invites').removeClass('active');
    });

    $("#createEvent").click(function () {
        $('#events-modal').css('display', 'block');
        $('#color').css("display", "none");
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

    $(".deleteEvent").click(function () {
        $(this).find('input').each(function(){
            let $data = {};
            $data["id"] = $(this).val();
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/deleteEvent",
                data: JSON.stringify($data),
                dataType: 'json',
                // success: () => {
                //     $('#events-modal').css('display', 'none');
                //     $('#modal').css('display', 'block');
                // },
                // error: () => {
                //     $('#events-modal').css('display', 'none');
                //     $('#modal').css('display', 'block');
                // }
            });
        });
    })
});