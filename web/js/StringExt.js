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
    /**
     * 为了兼容IE5中的字符串无法通过索引值来取数
     */
    var getIndex = function() {
        if (!String.prototype.getIndex) {
            String.prototype.getIndex = function(index){
                if (this.length > index) {
                    if (this[index] == undefined) {
                        return this.split('')[index];
                    } else {
                        return this[index];
                    }
                } else {
                    return undefined;
                }
            };
        }
    };
    return {
        splitOption : splitOption,
        splitExt : splitExt,
        getIndex : getIndex
    };
})();
