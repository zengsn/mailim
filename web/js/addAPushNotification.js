/**
 * Created by IBAS on 2017/2/13.
 */
/**
 * 应事先引入 domUtil.js 文件
* */
var addAPushNotification = (function(){
    //添加推送的父级元素
    var parent_element = null;
    var setParent = function(parent_ele){
        if (parent_ele instanceof Element) {
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
                    p.classList.add('paraStyle');
                    bodyDiv.appendChild(p);
                    //创建脚部
                    var footDiv = domUtil.newEle('div');
                    footDiv.classList.add("panel-footer","text-left");
                    footDiv.appendChild(domUtil.newEleWithConten('span',obj.score));
                    var rightA = domUtil.newEleWithConten('a','查看');
                    rightA.href = obj.enterUrl;
                    rightA.style.float = 'right';
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
    return {
        addNewEleByObj : addNewEleByObj,
        addNewEle : addNewEle,
        setParent : setParent
    };
})();
