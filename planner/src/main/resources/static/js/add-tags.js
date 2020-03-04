$(document).ready(function(){
    $(function(){ 
      $("#tag").on({
        focusout : function() {
          var txt = this.value.replace(/[^a-z0-9\+\-\.\#]/ig,'');
          if(txt) {
            $("<span/>", {
                text:txt.toLowerCase(),
                appendTo:"#tags",
                class:"dashfolio-tag"
            });
        }
          this.value = "";
        },
        keyup : function(ev) {
          if(/(188|13)/.test(ev.which)) $(this).focusout(); 
        }
      });
      $('.tags').on('click', 'span', function() {
        if(confirm("Remove "+ $(this).text() +"?")) $(this).remove(); 
      });
    
    });
    
    });