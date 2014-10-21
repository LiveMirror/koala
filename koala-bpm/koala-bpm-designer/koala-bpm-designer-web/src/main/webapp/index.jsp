<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<%
	Long time = new Date().getTime();
    String gunvorServerUrl = org.openkoala.bpm.designer.infra.EnvConfigHelper.getInstance().getProperty("gunvor.server.url");
%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <title>Koala流程设计平台</title>
        <meta charset="utf-8">
        <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
	    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
	    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
        <link href="<c:url value='/css/koala-tree.css' />?time=<%=time%>" rel="stylesheet">
        <link href="<c:url value='/css/jbpmDesigner.css' />?time=<%=time%>" rel="stylesheet">
	    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />"   rel="stylesheet">  
	    <script>
	        var contextPath = '${pageContext.request.contextPath}';
	    </script>
    </head>
  	<body>
            <div class="g-head">
                <nav class="navbar navbar-default">
                     <a class="navbar-brand" href="http://openkoala.org/display/koala/Home" target="_blank"><img src="<c:url value='images/global.logo.png'/>"/>Koala流程设计平台</a>
                     <div class="collapse navbar-collapse navbar-ex1-collapse">
                           <div class="btn-group navbar-right">
                               <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                   <i class="glyphicon glyphicon-user"></i>
                                   <span>&nbsp;Admin</span>
                                   <span class="caret"></span>
                               </button>
                               <ul class="dropdown-menu" id="userManager">
                                   <li data-target="modifyPwd"><a href="#">修改密码</a></li>
                                   <li data-target="switchUser"><a href="#">切换用户</a></li>
                                   <li data-target="loginOut"><a href="#">注销</a></li>
                               </ul>
                           </div>
                       </div>
                </nav>
            </div>
		    <div class="g-body">
			      <div class="col-xs-2 g-sidec">
                    <ul class="nav nav-stacked first-level-menu">
                        <li class="active">
                            <a data-toggle="collapse" href="#bpmManagement"><i class="glyphicon glyphicon-list"></i>&nbsp;流程管理&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
                            <ul id="bpmManagement" class="second-level-menu in">
                                <div class="tree" id="menuTree"></div>
                            </ul>
                        </li>
                        <li>
                            <a data-toggle="collapse" href="#publishManagement"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;发布管理&nbsp;<i class="glyphicon glyphicon-chevron-left"></i></a>
                            <ul id="publishManagement" class="second-level-menu in">
                                <li class="submenu" data-role="openTab" data-target="/pages/jbpm/publish-url-list.html" data-title="流程引擎管理" data-mark="publishURLList"><a><i class="glyphicon glyphicon-hand-right"></i>&nbsp;流程引擎管理</a></li>
                            </ul>
                        </li>
                    </ul>
			    </div>
			    <div class="col-xs-10 g-mainc">
			        <ul class="nav nav-tabs" id="navTabs">
			            <li class="active"><a href="#home" data-toggle="tab">主页面</a></li>
			        </ul>
			        <div class="tab-content" id="tabContent">
			            <div id="home" class="tab-pane active"></div>
			        </div>
			    </div>
		    </div>
		    <div id="footer" class="g-foot">
		        <span>Copyright © 2011-2013 Koala</span>
		    </div>
    <script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-tree.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/lib/validate.js' />"></script>
    <script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/js/jbpmDesigner/jbpmDesigner.js' />?time=<%=time%>" ></script>
    </body>
</html>