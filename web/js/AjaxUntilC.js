/**
 * Created by IBAS on 2017/2/23.
 * 。。。好像先不用
 */
/**
 * 作用：
 *      重复异步请求直到达到某一个条件
 * */
var AjaxUntilC = (function(){
    /**
     * option     :  异步内容，包括完整的请求地址和请求内容
     *      url:
     *      data:
     *      complete:
     *      times:最多请求次数
     * condition  :  异步结束条件
     * fail       :  请求失败事件(有condition指定触发)
     * */
    var option = {
            url : "",
            data : {},
            complete : function(){},
            times : 4
        },
        condition = function(){
        },
        fail = function(){
        };
    //初始化事件，主要是各种参数的初始化和完成检查
    function init(){
        option.condition = option.condition || option;
        option.fail = option.fail || fail;
    };
    //触发事件，包括请求开始和阻止请求过程中重复触发
    function invoke() {
        $.ajax({
            url : option.url,
            data : option.data,
            complete : option.complete
        });
    };
    return {
        init : init,
        invoke : invoke
    };
})();
