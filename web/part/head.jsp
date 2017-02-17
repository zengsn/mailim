<%@ page import="cn.sunibas.entity.TSkid" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/2/9
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="/translatorspace/css/head.css"/>

<!-- Fixed navbar -->
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="home.jsp">translator space</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <ul class="nav navbar-nav">
        <li class = "ibas_head_li"><a href="/translatorspace">首页</a></li>
        <li class = "ibas_head_li"><a href="#">分类</a></li>
        <li class = "ibas_head_li"><a href="/translatorspace/Manuscript/PushManuscript.jsp">发文稿</a></li>
        <%--<li class="dropdown">--%>
          <%--<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>--%>
          <%--<ul class="dropdown-menu">--%>
            <%--<li><a href="#">Action</a></li>--%>
            <%--<li><a href="#">Another action</a></li>--%>
            <%--<li><a href="#">Something else here</a></li>--%>
            <%--<li role="separator" class="divider"></li>--%>
            <%--<li class="dropdown-header">Nav header</li>--%>
            <%--<li><a href="#">Separated link</a></li>--%>
            <%--<li><a href="#">One more separated link</a></li>--%>
          <%--</ul>--%>
        <%--</li>--%>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <%-- if tsKid is login --%>
        <%if (session.getAttribute("kidInfo") != null) {
          %>
            <li class = "ibas_head_li"><a href = "#">
              <%=((TSkid)session.getAttribute("kidInfo")).getName()%>
              <span class="sr-only">小译信息，同时可以进入小译个人主页</span>
            </a></li>
            <li class = "ibas_head_li"><a href="admin_logout">登出</a></li>
          <%
        } else {
          %>
            <li class = "ibas_head_li"><a href="admin_login">登陆</a></li>
            <li class = "ibas_head_li"><a href="admin_register">注册</a></li>
          <%
        }%>
      </ul>
      <form class="navbar-form navbar-left">
        <div class="input-group">
          <input type="text" class="form-control" value="" placeholder="搜你喜欢"/>
          <span class="input-group-btn">
            <input type="submit" class="btn btn-default" value="GO!"/>
          </span>
        </div>
      </form>
    </div><!--/.nav-collapse -->
  </div>
</nav>
