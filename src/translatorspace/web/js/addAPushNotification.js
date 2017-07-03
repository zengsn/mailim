/**
 * Created by IBAS on 2017/2/13.
 */
/**
 * 应事先引入 domUtil.js 文件
 * 应事先引入 sessionStorageOP.js 文件
* */
/**
 * 使用方法：
 *  1）初始化父级元素 addAPushNotification.setParent(element object)
 *  2）在父级元素中插入对象 addAPushNotification.addNewEleByObj(object)
 *      或者
 *      addAPushNotification.addNewEle()
 */
var addAPushNotification = (function(){
    //添加推送的父级元素
    var parent_element = null;
    var aLabelClickEvent = null;
    var setParent = function(parent_ele){
        var ele = Element || Object;
        if (parent_ele instanceof Object) {
            parent_element = parent_ele;
        } else {
            throw new Error(parent_ele + "is not a element");
        }
    };
    /**
     * obj 是一个对象，其中包括推文的标题，内容和相关信息
     * domUtil.newEle 创建新元素
     * obj = {
     *  title : title,
     *  content : content,
     *  score : score,
     *  enterUrl : enterUrl
     * };
     * */
    var addNewEleByObj = function(obj) {
        if (parent_element) {
            var mainDiv = domUtil.newEle('div');
            mainDiv.classList.add("ibasPanelSize","text-center");
            {   //创建 panel div 对象
                var panDiv = domUtil.newEle('div');
                panDiv.classList.add("panel","panel-default");
                {
                    //创建头部
                    var headDiv = domUtil.newEle('div');
                    headDiv.classList.add("panel-heading","text-left");
                    headDiv.appendChild(domUtil.newEleWithConten('span',obj.title));
                    //创建主体
                    var bodyDiv = domUtil.newEle('div');
                    bodyDiv.classList.add("panel-body");
                    var p = domUtil.newEleWithConten('p',obj.content);
                    $(p).addClass('paraStyle');
                    //p.classList.add('paraStyle');
                    bodyDiv.appendChild(p);
                    //创建脚部
                    var footDiv = domUtil.newEle('div');
                    footDiv.classList.add("panel-footer","text-left");
                    footDiv.appendChild(domUtil.newEleWithConten('span',obj.score));
                    var rightA = domUtil.newEleWithConten('a','查看');
                    //rightA.href = obj.enterUrl;
                    rightA.style.float = 'right';
                    rightA.textPartUrl = obj.enterUrl;
                    rightA.style.cursor = 'pointer';
                    $(rightA).on('click',aLabelClickEvent);
                    footDiv.appendChild(rightA);
                }
                panDiv.appendChild(headDiv);
                panDiv.appendChild(bodyDiv);
                panDiv.appendChild(footDiv);
            }
            mainDiv.appendChild(panDiv);
            parent_element.appendChild(mainDiv);
        } else {
            throw new Error('Please set parent_element first .');
        }
    };
    var addNewEle = function(title,content,score,enterUrl) {
        addNewEleByObj({
            title : title,
            content : content,
            score : score,
            enterUrl : enterUrl
        });
    };
    var setALabelClickEvent = function(fn){
        aLabelClickEvent = fn;
    };
    return {
        addNewEleByObj : addNewEleByObj,
        addNewEle : addNewEle,
        setParent : setParent,
        setALabelClickEvent : setALabelClickEvent
    };
})();

/**
 * 这是一个特殊功能的脚本，不用过多的考虑到再次封装问题
 * 这里仅仅考虑为一次完成封装，同时作为一个单例来运行
 * 同时这里脚本仅仅完成部分，主要还是通过参数来进行配置
 * ================================================
 * 这里的配置包括
 * 1）url地址的配置
 * 2）请求参数配置
 * 3）请求完成回调函数的配置
 * 4）stop状态改变机制
 * ================================================
 * 注意事项
 * 1）这里的option为请求参数
 * 2）stop也是将会是该对象停止
 * 3）使用option中的cdata事件修改请求参数
 */
/**
 * 使用方法：
 * 1）初始化参数       getTextPart.setOption();
 * 2）调用初始化函数   getTextPart.init();
 * 3）调用请求函数    getTextPart.requireMore();
 * =================================================
 * 其中参数格式如下
 * {
 *      //异步元素
 *      tarEleId : 'ibas-center',
 *      //请求位置
 *      tarUrl : "/translatorspace/home_getChip",
 *      //请求参数
 *      data : {
 *          arg1 : 'arg1',
 *          arg2 : 'arg2'
 *      },
 *      //处理请求完成后的响应文本
 *      //这里的格式是固定的，
 *      //其中第一个函数为getTextPart封装好的函数，负责处理每一个TextPart对象
 *      //responseText为响应文本
 *      //这里只要编写好将TextPart从responseText中分离出来的代码，
 *      //同时适当修改data字段即可，
 *      //这里data字段使用this.data.arg1 = 'new arg1' 进行修改
 *      dearRet : function(dearObj,responseText)
 * }
 * */
var getTextPart = (function() {
    var option = null,          //请求参数
        isRequiring = false,    //是否在请求中，防止某些事件导致的重复多次请求
        defaultOption = {
            cdata : function() {},
            dearRet : function() {},//处理ajax结果
            stop : false,       //停止加载标记（true时生效）
            data : {},
            tarEleId : '',      //目标元素的id
            tarUrl : ''         //请求的url
        },                      //默认请求参数
        firstInit = true;
    function setOption(option_) {
        option = option_;
    };
    function init() {
        //初始化事件，最好在window.onload中调用
        //包括参数的初始化
        addAPushNotification.setParent(
            document.getElementById(option.tarEleId)
        );
        if (firstInit) {
            firstInit = false;
            addAPushNotification.setALabelClickEvent(openOneTextPart);
        }
        option = option || defaultOption;
        option.stop = false;
        if (!option.invokeStop) {
            option.invokeStop = false;
        }
        invoke();
    };
    function requireMore() {
        if (isRequiring) {
            return;
        }
        if (option.stop) {
            return;
        }
        isRequiring = true;
        $.ajax({
            url : option.tarUrl,
            data : option.data,
            complete : option.completeFn || completeFn
        });
    };
    function completeFn(msg) {
        if (msg.status == 200 && msg.readyState == 4) {
            option.dearRet(dearSimgleTextPartObject,msg.responseText);
            isRequiring = false;
            invoke();
        }
        isRequiring = false;
    };
    /**
     * 这里是一个比较奇怪的设定，对于闭包，一般不会设定该项，因为这样会将复用的能力被降低
     * 另外，为了符合后面可能出现的特殊情况，这里使用了默认invokeStop属性优先的机制
     * 建议是将 defaultInvoke invoke invokeStop 三个来自于不同对象的属性同时删除
     * 这里考虑到不会被过分复用就不再计较细节的不合理
     */
    function defaultInvoke() {
        if (document.body.scrollHeight <= window.screen.height) {
            requireMore();
        }
    };
    function invoke() {
        if (option.invokeStop) {
            return;
        } else {
            defaultInvoke();
        }
    };
    function dearSimgleTextPartObject(obj) {
        addAPushNotification.addNewEleByObj({
            title : 'title : overTime : ' + obj.overTimes,
            content : obj.partText,/*'uuid : ' + obj.TSuuid + "\r\n" +
             'TSindex : ' + i.TSindex + "\r\n" +
             'fromLanguage : ' + obj.fromLanguage + "\r\n" +
             'toLanguage : ' + obj.toLanguage + "\r\n" +
             'money : ' + obj.money,*/
            score : obj.score,
            enterUrl : '/translatorspace/Chip_tagChip?tag=' + obj.urlTag
        });
    };
    function openOneTextPart() {
        $.ajax({
            url : this.textPartUrl,
            complete : function(msg) {
                if (msg.readyState == 4 && msg.status == 200) {
                    var obj = JSON.parse(msg.responseText);
                    if (obj.status == 200) {
                        sessionStorageOP[uselessObj.textPart]
                            .set(
                                obj
                                    .packageTSTextPart);
                        window.location.href = "/translatorspace/Manuscript/TranslateOneChip.jsp";
                    } else if (obj.status == 500) {
                        //没有登陆
                        alert("请先登陆！");
                    } else {
                        alert(obj.retStr);
                    }
                }
            }
        });
    };
    return {
        init : init,
        requireMore : requireMore,
        setOption : setOption
    };
})();

var requireObjectForGetTextPart = function() {
    this.tarEleId = '';
    this.tarUrl = '';
    this.data = {};
    this.invokeStop = false;
    this.dearRet = function(dearObj,responseText){}
};
