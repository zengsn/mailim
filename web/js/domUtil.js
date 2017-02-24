/**
 * Created by IBAS on 2017/2/13.
 */

var domUtil = (function(){
    var newEle = function(str){
        if (typeof str === 'string'){
            return document.createElement(str);
        } else {
            return null;
        }
    };
    var newEleWithConten = function(str,content_) {
        if (typeof str === 'string'){
            var ele = document.createElement(str);
            ele.innerText = content_;
            return ele;
        } else {
            return null;
        }
    };
    var getTar = function(tar){
        if (tar instanceof Element) {
            return tar;
        } else if (tar instanceof Array) {
            if (tar.length > 0) {
                return getTar(tar[0]);
            }
        } else if ((typeof tar).toLowerCase() === 'string') {
            var ch = tar.getIndex(0);
            if (ch == '.') {
                //class
                return document.getElementsByClassName(tar.substr(1))[0];
            } else if (ch == '#') {
                //id
                return document.getElementById(tar.substr(1));
            } else {
                //ele tag name
                return document.getElementsByTagName(tar)[0];
            }
        }
        throw new Error('未能找到元素');
        return null;
    };
    return {
        newEle : newEle,
        newEleWithConten : newEleWithConten,
        getTar : getTar
    };
})();