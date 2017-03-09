/**
 * Created by sunbing on 17-3-5.
 */

/**
 * <link href="/translatorspace/css/TranslateChip.css" rel="stylesheet">
 *     实现引入jq和bootstrap,domUtil,sessionStorageOP
 * 显示翻译好的文稿碎片
 * */

var ShowTranslateTChip = (function(parentEle,tagUrl,$,sessionStorageOP){
        //所有的对象将添加到这里
    var parEle = parentEle,
        //用于异步请求
        tagUrl = tagUrl,
        //curkidid = curkidid,
        domUtils = domUtil,
        sessionStorageOP = sessionStorageOP,
        //这里将jq放置到闭包中加快js的运行速度
        jq = $,
        defaultGetChipUrl = "/translatorspace/mark_getTChip",
        defaultMarkChipUrl = "/translatorspace/mark_setMark",
        defaultGetMarkUrl = "/translatorspace/mark_getMarks",
        defaultSubmitChip = "/translatorspace/translate_translate",
        supportColor = "green",
        againstColor = "pink",
        isFirst = true;
    //构建一个基本的框架（对于单独一个文稿碎片的译文）
    /**
     * tskidid : 翻译者的id
     * mark ： 评分
     * */
    var initOneTC = function(tssuuid,mark,isSelf) {
        var mainDiv = domUtils.newEle("div");
        if (isSelf || isFirst) {
            $(mainDiv).addClass("panel panel-info ibas-TChip");
        } else {
            $(mainDiv).addClass("panel panel-default ibas-TChip");
        }
        //头部
        mainDiv.appendChild(createHeader(mark,tssuuid,isSelf,isFirst));
        //加载内容
        mainDiv.appendChild(createBody(tssuuid,isSelf,isFirst));
        parEle.appendChild(mainDiv);
        isFirst = false;
    };
    var createHeader = function(mark,tssuuid,isSelf,nullText) {
        //创建头部
        var headDiv = domUtils.newEle("div");
        var markI = domUtils.newEleWithConten("i",mark || "Create My translate");
        var openI = domUtils.newEle("i");
        $(headDiv).addClass("panel-heading");
        $(markI).addClass("glyphicon glyphicon-fire");
        $(openI).addClass("ibas-TCopen glyphicon glyphicon-chevron-left");
        openI.cur = "left";
        openI.tog = "down";
        openI.opened = false;
        openI.tssuuid = tssuuid;
        openI.isSelf = isSelf;
        openI.opened = !isSelf && isFirst;
        $(openI).click(function(){
            if (!this.opened) {
                //加载内容
                loadOneTC(this);
                this.opened = true;
            }
            $("#ct_" + this.tssuuid).collapse("toggle");
            $(this).removeClass("glyphicon-chevron-" + this.cur);
            $(this).addClass("glyphicon-chevron-" + this.tog);
            var tmp = this.cur;
            this.cur = this.tog;
            this.tog = tmp;
            return false;
        });
        headDiv.onclick = function () {
            openI.click.apply(openI);
        };
        headDiv.appendChild(markI);
        headDiv.appendChild(openI);
        return headDiv;
        //mainDiv.appendChild(headDiv);
    };
    var createBody = function(tssuuid,isSelf,isFirst) {
        var conDiv = domUtils.newEle("div");
        var ctDiv = domUtils.newEle("div");
        conDiv.id = "ct_" + tssuuid;
        conDiv.oncontextmenu = function(){return false;};
        $(conDiv).addClass("panel-collapse collapse");
        if (!isSelf && isFirst) {
            createText($(conDiv));
        } else {
            var markUl = domUtils.newEle("ul");
            var loading = domUtils.newEle("span");
            $(ctDiv).css({textAlign: "center"});
            conDiv.appendChild(ctDiv);
            $(markUl).addClass("ibas-mark-ul");
            $(loading).addClass("glyphicon glyphicon-refresh ibas-loadingCP");
            ctDiv.appendChild(loading);
            conDiv.appendChild(markUl);
        }
        return conDiv;
        //mainDiv.appendChild(conDiv);
    };
    var loadOneTC = function(obj) {
        /*
         translatorspace/mark_getTChip
         tagUrl=056140609688411hg6asu2ev3d9p60fs9,
         tssuuid
        * */
        $.ajax({
            url : defaultGetChipUrl,
            data : {
                tagUrl : tagUrl,
                tssuuid : obj.tssuuid
            },
            complete : function(m) {
                if (m.status == 200 && m.readyState == 4) {
                    var json = JSON.parse(m.responseText),
                        src = json.content,
                        dis = "",
                        len = src.length;
                    if (obj.isSelf) {
                        var div = $("#ct_" + obj.tssuuid + " div"),
                            tdiv = div.parent();
                        div.remove();
                        createText(tdiv,src,json.tssuuid||"");
                        tdiv.children("ul").remove();
                    } else {
                        for (var i = 0;i < len;i++) {
                            if (src[i] == "\r") {
                                continue;
                            } else if (src[i] == '\n') {
                                dis += "<br/>";
                            } else {
                                dis += src[i];
                            }
                        }
                        //这里继续请求下一个内容
                        var div = $("#ct_" + obj.tssuuid + " div");
                        div.css({textAlign : "left"});
                        div.html(dis);
                        div[0].tssuuid = json.tssuuid;
                        div[0].isOver = json.false;
                        getMark(obj.tssuuid,$("#ct_" + obj.tssuuid + " div"));
                    }
                }
            }
        });
    };
    var saveTextToServer = function() {
    };
    var createText = function(tDiv,text_,tssuuid) {
        var textArea = domUtils.newEle("textarea"),
            saveBtn = domUtils.newEleWithConten("button"," save"),
            submitBtn = domUtils.newEleWithConten("button"," upload");
        tssuuid = tssuuid || "";
        { //关于文本框
            var $textArea = $(textArea);
            textArea.key = tagUrl;
            $textArea.css({width:"100%",resize:"none",height:"100px",border : 'none',outline : 'none',padding:"0 5px"});
            if (sessionStorageOP[tagUrl]) {} else {
                sessionStorageOP.insert(tagUrl);
            }
            text_ = text_ || sessionStorageOP[tagUrl].get();
            if (text_) {
                $textArea.show(function(){
                    var h = this.scrollHeight + 5;
                    this.style.height = (h > 100)? h : 100;
                });
                $textArea.val(text_);
                sessionStorageOP[tagUrl].set(text_);
            }
            tDiv.append(textArea);
            $textArea.keydown(function(e){
                if (e.ctrlKey == true && e.keyCode == 83) {
                    //此时按下了ctrl+s建
                    //console.log(this.value);
                    sessionStorageOP[this.key].set(this.value);
                    return false;
                } else {
                    submitBtn.disabled = false;
                }
            });
            $textArea.change(function() {
                submitBtn.disabled = false;
            });
            submitBtn.disabled = true;
        }
        {
            //关于几个按钮
            var $saveBtn = $(saveBtn),
                $submitBtn = $(submitBtn);
            $saveBtn.addClass("btn btn btn-default glyphicon glyphicon-floppy-saved");
            $submitBtn.addClass("btn btn btn-default glyphicon glyphicon-floppy-open");
            $saveBtn.css({width : '40%'/*,float : "left"*/,margin: '4px 0 4px 5%'});
            $submitBtn.css({width : '40%',float : 'right',margin: '4px 5% 4px 0'});
            tDiv.append(saveBtn);
            tDiv.append(submitBtn);
            $saveBtn.click(function(){
                sessionStorageOP[tagUrl].set(textArea.value);
            });
            submitBtn.tssuuid = tssuuid;
            $submitBtn.click(function() {
                var ele = this;
                $.ajax({
                    url : defaultSubmitChip,
                    method : "post",
                    data : {
                        //translateContent : textArea.value,
                        chipContent : textArea.value,
                        tagUrl : tagUrl,
                        tssuuid : submitBtn.tssuuid
                    },
                    complete : function(m) {
                        if (m.status == 200 && m.readyState == 4) {
                            //
                            ele.disabled = true;
                            var json = JSON.parse(m.responseText);
                            if (json.status == 200) {
                                submitBtn.tssuuid = json.retStr;
                            }
                        }
                    }
                });
            });
        }
        return textArea;
    };
    var markOneTC = function() {
        //mark tagUrl tssuuid
        $.ajax();
    };
    var getMark = function(tssuuid,targetEle) {
        /*
         translatorspace/mark_getMarks
         tssuuid=
         * */
        $.ajax({
            url : defaultGetMarkUrl,
            data : {
                tssuuid : tssuuid
            },
            complete : function(m) {
                if (m.status == 200 && m.readyState == 4) {
                    var json = JSON.parse(m.responseText);
                    if (json.status) {
                        //成功
                        if (json.retStr.length == 0) {
                            //没有评价
                            console.log("get Mark success but not commention");
                            addSupportAndAgainst($(targetEle.selector.split(" ")[0]).children("ul"),tssuuid);
                        } else {
                            //已经评价过
                            var tmark = parseInt(json.retStr) | 1;
                            $(targetEle.selector.split(" ")[0]).children("ul").remove();
                            console.log("get Mark success commention is " + json.retStr);
                            $(targetEle.selector.split(" ")[0]).parent()
                                .css({
                                    borderColor : (tmark > 0)?supportColor:againstColor
                                });
                        }
                    } else {
                        //请求失败
                        console.log("get Mark fail");
                        //这里应该放置回调函数
                    }
                }
            }
        });
    };
    var addSupportAndAgainst = function(markUl,tssuuid) {
        var markLiSupport = domUtils.newEle("li");
        var markLiAgainst = domUtils.newEle("li");
        $(markLiAgainst).addClass("ibas-mark-li glyphicon glyphicon-thumbs-down");
        $(markLiAgainst).css({float : "left"});
        $(markLiSupport).addClass("ibas-mark-li glyphicon glyphicon-hand-right");
        $(markLiSupport).css({float : "right"});
        markLiAgainst.mark = -1;
        markLiSupport.mark = 1;
        markLiAgainst.tssuuid = tssuuid;
        markLiSupport.tssuuid = tssuuid;
        markUl.append(markLiAgainst);
        markUl.append(markLiSupport);
        setMark(markLiAgainst);
        setMark(markLiSupport);
    };
    var setMark = function(ele) {
        //tagUrl tssuuid mark
        $(ele).click(function(){
            $.ajax({
                url : defaultMarkChipUrl,
                data : {
                    tagUrl : tagUrl,
                    tssuuid : this.tssuuid,
                    mark : this.mark
                },
                complete : function(m) {
                    if (m.status == 200 && m.readyState == 4) {
                        var json = JSON.parse(m.responseText);
                        if (json.status == 200) {
                            //成功
                            setMarkSuccess(ele);
                        } else {
                            //失败的回调函数
                            console.log("失败，代码是：" + json.status + " 信息：" + json.retStr);
                        }
                    }
                }
            });
        });
    };
    var setMarkSuccess = function(ele) {
        //修改tag的边框属性
        ele = $(ele);
        var tag = ele.parent().parent().parent();
        tag.css({borderColor : (ele.mark > 0) ? supportColor : againstColor});
        ele.addClass("ibas-markCP");
        ele.siblings().remove();
    };
    return {
        initOneTC : initOneTC
    };
});
