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
    <link href="/translatorspace/css/TranslateChip.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../part/head.jsp"></jsp:include>
<script>
  var labelInfo = {
    type: 1
      ,offset: 't' //具体配置参考：offset参数项
      ,content: '<div style="padding: 20px 80px;">请先选择一个文稿</div>'
      ,btn: '知道了'
      ,btnAlign: 'c' //按钮居中
      ,shade: 1 //不显示遮罩
      ,yes: function(){
        layer.closeAll();
        window.location.href = "/translatorspace";
      }
  }
</script>
<div class="container">
  <div class="textPart"></div>
    <div>
        <%--<form action="/translatorspace/translate_translate" method="post" id = 'form' enctype="multipart/form-data">--%>
            <%--<input id="textTag" hidden="hidden" type="text" value="" name="tagUrl"/>--%>
            <%--<input type="file" name="translateContent"/>--%>
            <%--<input type="submit"/>--%>
        <%--</form>--%>
    </div>
    <!--这里假设session中有一个当前用户的kidid-->
    <div id="overTranslate" class="">

    </div>
</div>

<script>
    //先判断是否已经拿到了对象并放入到了session中
    var tagUrl;
    var sttcp;
    //////这里假设session中有一个当前用户的kidid////////////////////
    //var kidid = "06267518688416qm4663hvdef344okak";
    ///////////////////////////////
    window.onload = function() {
        //sessionStorageOP[uselessObj.kidid].set(kidid);
        if (sessionStorageOP[uselessObj.textPart].get().length) {
            var o = JSON.parse(sessionStorageOP[uselessObj.textPart].get());
            var str = '';
            for (var i in o) {
                str += i + '\t' + o[i] + '<br/>';
            }
            var textTag = $('#overTranslate');
            $('.textPart').html(str);
            textTag.val(o['urlTag']);
            tagUrl = o['urlTag'];
            sttcp = ShowTranslateTChip(textTag[0],tagUrl,$,sessionStorageOP);
            getTChip();
        } else {
            layer.open(labelInfo);
            $('.layui-layer-shade').css({background:'rgb(245, 239, 255)'});
        }
    };
  //在前面判断通过后，想后端获取翻译结果
  function getTChip() {
      $.ajax({
          url : "/translatorspace/mark_getChipsStatus",
          data : {
              tagUrl :tagUrl
          },
          complete : function(m) {
              if (m.status == 200 && m.readyState == 4) {
                  console.log(m);
                  var json = JSON.parse(m.responseText);
                  //var kidid = sessionStorageOP[uselessObj.kidid].get();
                  var overT = $("#overTranslate");
                  var domUtils = domUtil;
                  if (json.status == 200) {
                      var tagtsuuid = json.retStr,
                          map = json.map;
                      if (tagtsuuid in map) {
                          sttcp.initOneTC(tagtsuuid,map[tagtsuuid],true);
                          delete map[tagtsuuid];
                      } else {
                          sttcp.initOneTC(tagtsuuid,map[tagtsuuid],false);
                      }
                      for (var i in map) {
                          sttcp.initOneTC(i,map[i]);
                      }
                  }
              }
          }
      });
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
<script src="/translatorspace/layer/layer.js"></script>
<script src="/translatorspace/js/ibasConstructorForAutoObject.js"></script>
<script src="/translatorspace/js/sessionStorageOP.js"></script>
<script src="/translatorspace/js/ShowTranslateTChip.js"></script>
</body>
</html>
