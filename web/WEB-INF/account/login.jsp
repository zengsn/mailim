<%--
  Created by IntelliJ IDEA.
  User: Administrator
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
  <link href="/translatorspace/css/accountCss.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../../part/lr_head.jsp"></jsp:include>

<div class="container">
  <h1>登陆页</h1>
  <div class="ibasPanelSize text-center">
    <div class="panel panel-default">
      <div class="panel-heading text-left"><span>输入登陆信息</span></div>
      <div class="panel-body">
        <form class="text-center" action="admin_login" method="post">
          <div class="form-group">
            <input type="text" class="form-control" placeholder="小译名称" name="tSkid.name"/>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" placeholder="小译密码" name="tSkid.pwd"/>
          </div>
          <input type="submit" style = "width: 98%;" class="btn btn-primary btn-group-lg" value="登陆"/>
        </form>
      </div>
      <div class="panel-footer text-left">
        <a href = "#">忘记密码了？</a>
        <a href = "#" style = "float : right;">还没有账号？</a>
      </div>
    </div>
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
</body>
</html>
