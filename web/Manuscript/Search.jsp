<%@ page import="cn.sunibas.action.retObject.PackageTSTextPart" %>
<%--
  Created by IntelliJ IDEA.
  User: IBAS
  Date: 2017/2/6
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title></title>    <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Fixed Top Navbar Example for Bootstrap</title>

  <!-- Bootstrap core CSS -->
  <link href="/translatorspace/css/bootstrap.min.css" rel="stylesheet">

  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <link href="/translatorspace/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

  <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
  <!--[if lt IE 9]><script src="/translatorspace/js/ie8-responsive-file-warning.js"></script><![endif]-->
  <script src="/translatorspace/js/ie-emulation-modes-warning.js"></script>

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="/translatorspace/js/html5shiv.3.7.3.min.jss"></script>
  <script src="/translatorspace/js/respond.1.4.2.min.js"></script>
  <![endif]-->
  <script src="/translatorspace/js/domUtil.js"></script>
  <script src="/translatorspace/js/ibasConstructorForAutoObject.js"></script>
  <script src="/translatorspace/js/sessionStorageOP.js"></script>
  <script src="/translatorspace/js/addAPushNotification.js"></script>
  <style>
    .TextLabel {
      box-sizing: border-box;
      padding: 1px 8px;
      background: #a8fbbe;
      margin: 5px;
      border-radius: 8px;
      font-size: 18px;
      cursor : pointer;
    }
    .TextLabelSelected {
      background: #b2d1ff;
      font-size: 20px;
      padding: 0 8px;
    }
  </style>
</head>
<body>

<jsp:include page="../part/head.jsp"></jsp:include>

<div class="container">
  <div id = 'ibas-label'>
    <!--这里放置标签集合-->
  </div>
  <div id = 'ibas-center'>

  </div>
</div>
<script>
  var clickTar = null;
  var requireObject = new requireObjectForGetTextPart();
  requireObject.tarEleId = "ibas-center";
  requireObject.data = {
    label : ""
  };
  requireObject.tarUrl = "/translatorspace/search_getTextPart";
  //requireObject.invokeStop = true;
  requireObject.dearRet = function(dearFn,responseText) {
    //
    var json = JSON.parse(responseText);
    if (json.status != 200) {
      //这里先假定其他状态都是结束
      this.stop = true;
    } else {
      json.data.forEach(function(i){
        dearFn(i);
      });
    }
  };
  window.onload = function(){
    //加载标签
    initAddLabel();
  };
  function initAddLabel() {
    //清除保存在服务器的缓存
    $.ajax({
      url : "/translatorspace/search_getTextPart?label="
    });
    $.ajax({
      url : "/translatorspace/search_getTextLabel",
      complete : function(msg) {
        var ibasLabel = document.getElementById('ibas-label');
        console.log(msg.responseText);
        labels = JSON.parse(msg.responseText).labels;
        if (labels.length) {
          labels.forEach(function(i) {
            var li = domUtil.newEleWithConten("label",i);
            li.label = i;
            $(li).addClass("TextLabel");
            $(li).on('click',liEvent);
            ibasLabel.appendChild(li);
          });
        }
      }
    });
  };
  var liEvent = function() {
    $("#ibas-center").children(".ibasPanelSize").remove();
    if (clickTar) {
      $(clickTar).on('click',liEvent);
      $(clickTar).removeClass('TextLabelSelected');
    }
    clickTar = this;
    $(this).off('click',liEvent);
    $(this).addClass('TextLabelSelected');
    invoke(this.label);
  };
  function invoke(label){
    if (!label) {
      return;
    }
    requireObject.data.label = label;
    getTextPart.setOption(requireObject);
    getTextPart.init();
    window.onscroll = function(){
      if (document.body.clientHeight+document.body.scrollTop + 100 >= document.body.scrollHeight) {
        getTextPart.requireMore();
      }
    };
    getTextPart.requireMore();
  }
</script>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/translatorspace/js/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="/translatorspace/js/vendor/jquery.min.js"><\/script>')</script>
<script src="/translatorspace/js/bootstrap.min.js"></script>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="/translatorspace/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
