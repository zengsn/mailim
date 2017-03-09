/**
 * Created by IBAS on 2017/2/24.
 */
/***
 * 编写理由：
 *      方便字段的修改和查询插入操作
 */
/**
 * 事先引入 ibasConstructorForAutoObject.js 文件
 * */
//确实是没有用处的，但是这里这样写的目的是使ide能智能提示
var uselessObj = {
    textPart : 'textPart',
    translateHic : 'translateHic'
};
var sessionStorageOP = (function(){
    ress = ibasConstructorForAutoObject({
        opObject : sessionStorage,
        set : function(val){
            if (val instanceof Object) {
                sessionStorage.setItem(this.name,JSON.stringify(val));
            } else {
                sessionStorage.setItem(this.name,val);
            }
        },
        get : function() {
            return sessionStorage.getItem(this.name);
        }
        //remove : function(name_) {
        //    sessionStorage.removeItem(name_);
        //}
    });
    for (var i in uselessObj) {
        ress.insert(i);
        if (ress[i].get() == null) {
            ress[i].set('');
        }
    }
    return ress;
})();