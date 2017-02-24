/**
 * Created by IBAS on 2017/2/24.
 */
/**
 * 将对象封装为更易操作的对象
 * 使用方法
 * var o = ibasConstructorForAutoObject();      //创建一个封装对象
 * var o.insert('pro1');                        //为对象添加属性
 * o.pro1.set('val');                           //设置o对象的pro1的属性值
 * o.pro1.get();                                //获取属性值
 * o.remove('pro1');                            //删除属性
 * ==================================================================
 * var o = ibasConstructorForAutoObject(obj);   //将对象做更高级的封装
 * ....使用方式如上
 * obj = {
 *      opObject : obj,             //操作对象
 *      set : function(val){},      //重写默认的set方法
 *      get : function(){},         //重写默认的get方法
 *      remove : function(name){}   //为remove方法添加扩展 ! 这里是扩展
 * };
 * //这里重写使用的属性是this.name来获取当前将操作的对象，name为属性名称
 * //这里remove方法使用name来操作对象，name为属性名称
 * */
var ibasConstructorForAutoObject = (function(storeObject){
    return (function(b){
        var opObject = {
                insert : null,
                remove : function(name_) {
                    if (this[name_]) {
                        delete this[name_];
                    }
                }
            };
        var setFn = null;
        var getFn = null;
        if (b) {
            storeObject = b.opObject;
            setFn = b.set;
            getFn = b.get;
            if (b.remove) {
                opObject.remove = function(name_){
                    if (this[name_]) {
                        delete this[name_];
                    }
                    b.remove(name_)
                };
            }
        } else {
            storeObject = {};
        }
        var defaultInsert = function(name_){
            this[name_] = {
                get : defaultGet(),
                set : defaultSet(),
                name : name_
            };
        };
        var defaultGet = function() {
            if (getFn) {
                return getFn;
            } else {
                return function() {
                    return storeObject[this.name];
                }
            }
        };
        var defaultSet = function() {
            if (setFn) {
                return setFn;
            } else {
                return function(val){
                    storeObject[this.name] = val;
                }
            }
        };
        opObject.insert = defaultInsert;
        return opObject;
    })(storeObject);
});

/***
 * 重构sessionStorage
    var ress = ibasConstructorForAutoObject({
        opObject : sessionStorage,
        set : function(val){
            if (val instanceof Object) {
                this.type_ = {};
                sessionStorage.setItem(this.name,JSON.stringify(val));
            } else {
                sessionStorage.setItem(this.name,val);
            }
        },
        get : function() {
            if (this.type_) {
                return JSON.parse(sessionStorage.getItem(this.name));
            } else {
                return sessionStorage.getItem(this.name);
            }
        },
        remove : function(name_) {
            sessionStorage.removeItem(name_);
        }
    });
 */
