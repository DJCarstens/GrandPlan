$(document).ready(function () {
    $('#create-event').click(function (e) {
        let $title = true;
        let $description = true;
        let $members = true;

        e.preventDefault();
        e.stopPropagation();
        if ($('#title').val() == '') {
            $('#titleError').html('Please provide a title for this event');
            $('#titleError').css('display', 'block');
            $title = false;
        }

        if ($('#description').val() == '') {
            $('#descriptionError').html('Please provide a description for this event');
            $('#descriptionError').css('display', 'block');
            $description = false;
        }

        if ($('#members').val() == '') {
            $('#membersError').html('Please add members to this event');
            $('#membersError').css('display', 'block');
            $members = false;
        }

        if($('#color').val() == '') {
            $("#color").prop("value", '#0096B1');
        }

        if($title && $description && $members){
            let $data = {};
            $data['title'] = $('#title').val();
            $data['description'] = $('#description').val();
            $data['members'] = $('#members').val();
            $data['start'] = $('#start').val();
            $data['end'] = $('#end').val();
            $data['allDay'] = $('#allDay').prop("checked") ? true : false;
            $data['tag'] = $('#tag').val();
            $data['color'] = $('#color').val();
            
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/createEvent",
                data: JSON.stringify($data),
                dataType: 'json',
                success: () => {
                    $('#events-modal').css('display', 'none');
                    $('#modal').css('display', 'block');
                },
                error: () => {
                    $('#events-modal').css('display', 'none');
                    $('#modal').css('display', 'block');
                }
            });
        }
    });

    $('#title').focus(function () {
        $('#titleError').css('display', 'none');
    });

    $('#description').focus(function () {
        $('#descriptionError').css('display', 'none');
    });

    $('#members').focus(function () {
        $('#membersError').css('display', 'none');
    });
});