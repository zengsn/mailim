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
    <link href="/translatorspace/css/homeCss.css" rel="stylesheet">
    <script src="js/domUtil.js"></script>
    <script src="js/ibasConstructorForAutoObject.js"></script>
    <script src="js/sessionStorageOP.js"></script>
    <script src="js/addAPushNotification.js"></script>
    <script src="js/ArrExt.js"></script>
    <script>
      ArrExt.copyTime();
    </script>
  </head>
  <body>

    <jsp:include page="part/head.jsp"></jsp:include>

    <div class="container" id = 'ibas-center'>
      <h1>主页</h1>
      <!--新建若干个随机-->
      <script>
        getTextPart.setOption(
                {
                  //异步元素
                  tarEleId : 'ibas-center',
                  //请求位置
                  tarUrl : "/translatorspace/home_getChip",
                  //请求数据
                  data : {
                    from : 0,
                    count : 2
                  },
                  //请求成功后回调处理数据
                  dearRet : function(dearOneObj,msgResponseText){
                    if (msgResponseText == '[]') {
                      window.onscroll = "";
                      this.stop = true;
                      return ;
                    }
                    (JSON.parse("{\"data\":" + msgResponseText + "}").data)
                            .forEach(function(i){
                              dearOneObj(i);
                            }
                    );
                    this.data.from += this.data.count;
                  }
                }
        );
        window.onload = function() {
            getTextPart.init();
            window.onscroll = function(){
                if (document.body.clientHeight+document.body.scrollTop + 100 >= document.body.scrollHeight) {
                    getTextPart.requireMore();
                }
            };
            var script = domUtil.newEle("script");
            script.src = "/translatorspace/js/bak.js";
            $("body").append(script);
        };
      </script>
    </div>

    <!-- Bootstrap core JavaScript
  ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/translatorspace/js/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="/translatorspace/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="/translatorspace/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/translatorspace/js/ie10-viewport-bug-workaround.js"></script>
    <script src="/translatorspace/js/bak.js"></script>
  </body>
</html>
