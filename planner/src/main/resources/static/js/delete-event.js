$(document).ready(function () {
    $(".delete-event").click(function() {
        let $items = [];
        $(this).parent().find('input').each(function(){
            $items.push($(this).val());
        });

        if($items.length == 1){
            let $id = "#" + $items[0];
            let $class = "." + $items[0];
            $($id).css("opacity", "0.2");
            $($class).toggle();
            $($class).css("position", "absolute");
            $($class).css("top", "45%");            
            $($class).css("left", "28%");
            $($class).css("z-index", "99");
            $($class + " .removeEvent").css("padding", "4%");
            $($class + " .removeEvent").css("width", "90%");
        }
        else{
            let $id = "#" + $items[1];
            let $class = "." + $items[1];
            $($id).css("opacity", "0.2");
            $($class).toggle();
            $($class).css("position", "absolute");
            $($class).css("top", "34%");            
            $($class).css("left", "5%");
            $($class).css("z-index", "99");
        }
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

    $(".updateEvent").click(function(){
        $(this).find('input').each(function(){
            console.log($(this).val());
        });
    });
});