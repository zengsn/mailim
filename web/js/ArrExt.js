/**
 * Created by IBAS on 2017/2/13.
 */

var ArrExt = (function (){
    var pushOption = {
        Distinct : 1,
        DropUnUse : 2,
        ShowIndex : 3
    };
    var copyTime = function() {
        if (!Array.prototype.copyTimes) {
            Array.prototype.copyTimes = function(times){
                var arr = this;
                var tar = [];
                for (var i = 0;i < times;i++) {
                    arr.forEach(function(j){
                        tar.push(j);
                    });
                }
                return tar;
            };
        }
    };
    var distinct = function() {
        if (!Array.prototype.distinct) {
            Array.prototype.distinct = function(){
                var arr = this,
                    dic = {},
                    ret = [];
                arr.forEach(function(i){
                    if (dic[i]) {} else {
                        dic[i] = 1;
                        ret.push(i);
                    }
                });
                return ret;
            };
        }
    };
    var pushExt = function() {
        if (!Array.prototype.pushExt) {
            Array.prototype.pushExt = function(item,option) {
                var ret = true,
                    arr = this,
                    index = -1;
                if (option == pushOption.Distinct || option == pushOption.ShowIndex) {
                    arr.forEach(function(i,j){
                        if (i == item) {
                            ret = false;
                            index = j;
                            return;
                        }
                    });
                }
                if (ret) {
                    this.push(item);
                }
                if (pushOption.ShowIndex == option) {
                    return index;
                }
                return ret;
            }
        }
    };
    var joinExt = function() {
        if (!Array.prototype.joinExt) {
            Array.prototype.joinExt = function(ch,option) {
                var ret = '',
                    arr = this,
                    j = 0;
                if (option == pushOption.DropUnUse) {
                    arr.forEach(function(i){
                        if (i) {
                            if (j) {
                                ret += ch + i;
                            } else {
                                j++;
                                ret = i;
                            }
                        }
                    });
                }
                return ret;
            };
        }
    };
    var add = function() {
        if (!Array.prototype.add) {
            Array.prototype.add = function() {
                var ret = this;
                for (var i = 0;i < arguments.length;i++) {
                    ret = ret.addOne(arguments[i]);
                }
                return ret;
            };
            Array.prototype.addOne = function(str){
                return this.pushExt(str,pushOption.Distinct);
            };
        }
    }();
    var remove = function() {
        if (!Array.prototype.remove) {
            Array.prototype.remove = function() {
                var ret = this;
                for (var i = 0;i < arguments.length;i++) {
                    ret = ret.removeOne(arguments[i]);
                }
                return ret;
            };
            Array.prototype.removeOne = function(str) {
                var ret = [];
                for (var i = 0;i < this.length;i++) {
                    if (this[i] == str) {} else {
                        ret.push(this[i]);
                    }
                }
                return ret;
            }
        }
    }();
    //兼容低版本IE
    var forEach = function(){
        if (Array.prototype.forEach == undefined) {
            //fn的格式和高版本中forEach中fn格式一致
            Array.prototype.forEach = function(fn) {
                for (var i = 0;i < this.length;i++) {
                    fn(this[i],i);
                }
            }
        }
    }();
    return {
        copyTime : copyTime,
        distinct : distinct,
        pushExt : pushExt,
        joinExt : joinExt,
        pushOption : pushOption
    };
})();
