<%@ page import="cn.sunibas.util.StaticObject" %>
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
  <title></title>
  <meta charset="utf-8">
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
  <link href="/translatorspace/css/ibasUL.css" rel="stylesheet">
  <script src="/translatorspace/js/ibasUL.js"></script>
  <link href="/translatorspace/css/ibasLB.css" rel="stylesheet">
  <script src="/translatorspace/js/ibasLB.js"></script>
  <!--我的一些扩展依赖-->
  <script src = "/translatorspace/js/StringExt.js"></script>
  <script src = "/translatorspace/js/HtmlExtern.js"></script>
  <script src = "/translatorspace/js/ArrExt.js"></script>
</head>
<body>

  <jsp:include page="../part/head.jsp"></jsp:include>

  <div class="container">
    <h1>发文稿</h1>
      <form action="/translatorspace/Manuscript_upload" method="post" id = 'form' enctype="multipart/form-data">
        <input type="text" name="tstext.title" value="" placeholder="title"/>
        <input
                type = 'file'
                id = 'ibasFile'
                multiple
                class = 'ibas-file-input'
                name = 'manuscript'/>
        <div>
          <input
                  type = 'text'
                  placeholder = 'Tag ...'
                  class = 'ibas-label-input'
                  id = 'ibasLabel'
                  name="label"/>
        </div>
        <div>
          <label>score</label><input type="number" value="0" name="tstext.score"/><br/>
          <label>money</label><input type="number" value="0" name="tstext.money"/>
        </div>
        <div>
          <%--<%--%>
            <%--StaticObject.language.keySet();--%>
          <%--%>--%>
          <!-- 这里的语言设置优化为下拉框 -->
          <label>fromLanguage</label><input type="number" value="0" name="tstext.fromLanguage"/><br/>
          <label>toLanguage</label><input type="number" value="0" name="tstext.toLanguage"/>
        </div>
        <input type="button" onclick="submit(form)" value="提交"/>
      </form>
      </div>
  </div>


  <!-- Bootstrap core JavaScript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="/translatorspace/js/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="/translatorspace/js/vendor/jquery.min.js"><\/script>')</script>
  <script src="/translatorspace/js/bootstrap.min.js"></script>
  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <script src="/translatorspace/js/ie10-viewport-bug-workaround.js"></script>
  <script>
    StringExt.getIndex();
    ArrExt.pushExt();
    ArrExt.joinExt();

    var file = document.getElementById('#ibasFile');
    var ibasFile0 = ibasFile('.ibas-file-input');
    ibasFile0.setFileTypeLimit(['txt']);
    ibasFile0.setLanguage({
      clickToSelectFile : '点击选择文件',
      reSelect : '重新选择',
      toMin : '最小化',
      toMax : '最大化',
      files : '个文件'
    });
    ibasFile0.init();
    var ibasLabel0 = ibasLabel("#ibasLabel");
    ibasLabel0.setDefaultMaxLabel(5);
    ibasLabel0.setLabelLength(10);
    ibasLabel0.init();
  </script>

</body>
</html>
