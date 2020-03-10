$(document).ready(function () {
    $(".deleteEvent").click(function() {
        $(this).find('input').each(function(){
            let $id = "#" + $(this).val();
            let $class = "." + $(this).val();
            $($id).css("opacity", "0.2");
            $($class).toggle();
            $($class).css("position", "absolute");
            $($class).css("top", "34%");            
            $($class).css("left", "5%");
            $($class).css("z-index", "99");
        });
    });

    $(".cancel-button").click(function() {
        $(this).find('input').each(function(){
            let $id = "#" + $(this).val();
            let $class = "." + $(this).val();
            $($id).css("opacity", "1");
            $($class).toggle();
        });
    });

    $(".cancel-delete-button").click(function() {
        $(this).find('input').each(function(){
            let $id = "#" + $(this).val();
            $($id).css("opacity", "1");
            $(this).parent().parent().toggle();
        });
    });

    $(".transferEvent").click(function() {
        $(this).find('input').each(function(){
            let $class = "." + $(this).val();
            $($class).toggle();
            $($class).css("position", "absolute");
            $($class).css("top", "34%");            
            $($class).css("left", "5%");
            $($class).css("z-index", "99");
        });

        $(this).parent().toggle();
    });

    $(".transfer-event").click(function(){
        let $items = [];
        $(this).parent().find('input').each(function(){
            $items.push($(this).val());
        });
        
        let $data = {};
        $data['id'] = $items[1];
        $data['hostUsername'] = $items[0];
        
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/transferEvent",
            data: JSON.stringify($data),
            dataType: 'json',
            success: () => {
                let $id = "#" + $items[1];
                $($id).css("opacity", "1");
                $(this).parent().parent().toggle();
            },
            error: () => {
                let $id = "#" + $items[1];
                $($id).css("opacity", "1");
                $(this).parent().parent().toggle();
            }
        });
    });

    $(".removeEvent").click(function() {
        let $items = [];
        $(this).parent().find('input').each(function(){
            $items.push($(this).val());
        });

        let $data = {};
        $data['id'] = $items[1];
        $data['hostUsername'] = $items[0];
        
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/deleteEvent",
            data: JSON.stringify($data),
            dataType: 'json',
            success: () => {
                let $id = "#" + $items[1];
                let $class = "." + $items[1];
                $($id).css("opacity", "1");
                $($class).toggle();
            },
            error: () => {
                let $id = "#" + $items[1];
                let $class = "." + $items[1];
                $($id).css("opacity", "1");
                $($class).toggle();
            }
        });
    });

    $(".updateEvent").click(function(){
        $(this).find('input').each(function(){
            console.log($(this).val());
        });
    });
});