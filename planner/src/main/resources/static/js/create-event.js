$(document).ready(function () {
    $('#create-event').click(function (e) {
        let $title = true;
        let $description = true;
        let $start = true;
        let $end = true;

        let $members = '';
        $('#invitedUsers').find('span').each(function(){
            let $first = $(this).text().split("(");
            let $second = $first[1].split(")");
            $members += (',' + $second[0]);
        });

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

        if($('#color').val() == '') {
            $("#color").prop("value", '#0096B1');
        }

        if($('#start').val() == ''){
            $('#startError').html('Please select a start date and time');
            $('#startError').css('display', 'block');
            $start = false;
        }

        if($('#end').val() == '' && $('#allDay').prop("checked") == false){
            $('#endError').html('Please select a end date and time');
            $('#endError').css('display', 'block');
            $end = false;
        }

        if($title && $description && $start && $end){
            let $data = {};
            $data['title'] = $('#title').val();
            $data['description'] = $('#description').val();
            $data['members'] = $members;
            $data['start'] = $('#start').val();
            $data['end'] = $('#end').val();
            $data['allDay'] = $('#allDay').prop("checked") ? true : false;
            $data['tag'] = null;
            $('#tags').find('span').each(function(){
                $data['tag'] = $(this).text();
            });
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

    $('#start').focus(function () {
        $('#startError').css('display', 'none');
    });

    $('#end').focus(function () {
        $('#endError').css('display', 'none');
    });

    $('#start-picker').datetimepicker({ useCurrent: false });
    $('#end-picker').datetimepicker({ useCurrent: false });
});