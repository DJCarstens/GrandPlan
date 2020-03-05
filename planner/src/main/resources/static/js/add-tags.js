$(document).ready(function(){
    $(function(){ 
      $("#tag").on({
        focusout : function() {
          var txt = this.value.replace(/[^a-z0-9\+\-\.\#]/ig,'');
          if(txt) {
            $("<span/>", {
                text:txt.toLowerCase(),
                appendTo:"#tags",
                class:"dashfolio-tag",
                id:"tag-element"
            });
            $('#tag').css('display', 'none');
            $('#tag').value(txt);
        }
          this.value = "";
        },
        keyup : function(ev) {
          if(/(188|13)/.test(ev.which)) $(this).focusout(); 
        }
      });
      $('.tags').on('click', 'span', function() {
        $(this).remove(); 
        $('#tag').css('display', 'block');
      });
    
    });
    
    });