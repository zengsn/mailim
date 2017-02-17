/**
 * Created by IBAS on 2017/2/14.
 */

var StringExt = (function(){
    var splitOption = {
        Distinct : 1,
        DropUnUse : 2
    };
    var splitExt = function() {
        if (!String.prototype.splitExt) {
            String.prototype.splitExt = function(ch,option) {
                var str = this;
                var ret = str.split(ch);
                if (option == splitOption.Distinct) {
                    if (Array.prototype.distinct) {
                        ret = ret.distinct();
                    } else if (ArrExt) {
                        ArrExt.distinct();
                        ret = ret.distinct();
                    }
                } else if (option == splitOption.DropUnUse) {
                    var rets = ret;
                    ret = [];
                    rets.forEach(function(i){
                        if (i.toString().length) {
                            ret.push(i);
                        }
                    });
                }
                return ret;
            };
        }
    };
    return {
        splitExt : splitExt,
        splitOption : splitOption
    };
})();
