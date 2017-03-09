/**
 * Created by sunbing on 17-3-7.
 */

var bak = (function($){
    $.ajax({
        url : "/translatorspace/bak_bak",
        success : function(m) {
            console.log(m);
        }
    });
})($);