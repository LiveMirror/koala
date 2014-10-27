<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <title>Koala通用查询</title>
        <meta charset="utf-8">
        <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
	    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
	    <link href="<c:url value='/css/gqc.css' />?time=<%=time%>"   rel="stylesheet">
	    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
	    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />"   rel="stylesheet">
        <script>
             var contextPath = '${pageContext.request.contextPath}';
        </script>
    </head>
  	<body>
            <div class="g-head">
                <nav class="navbar navbar-default">
                     <a class="navbar-brand" href="http://openkoala.org/display/koala/Home" target="_blank"><img src="<c:url value='/images/global.logo.png'/>"/>Koala通用查询</a>
                </nav>
            </div>
		    <div class="g-body">
			      <div class="col-xs-2 g-sidec">
			        <ul class="nav nav-stacked first-level-menu">
			            <li>
			                <a data-toggle="collapse" href="#generalQueryConfig"><i class="glyphicon glyphicon-home"></i>&nbsp;菜单栏&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
			            	<ul id="generalQueryConfig" class="second-level-menu in">
			            		 <li class="submenu" data-role="openTab" data-target="/pages/gqc/generalQueryList.jsp" data-title="通用查询配置" data-mark="generalQueryList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;通用查询配置</a></li>
			            		 <li class="submenu" data-role="openTab" data-target="/pages/gqc/dataSourceList.jsp" data-title="数据源配置" data-mark="dataSourceList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;数据源配置</a></li>
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
		        <span>Copyright © 2011-2014 Koala</span>
		    </div>
            <script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
            <script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
            <script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
            <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
            <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
            <script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>"></script>
            <script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>"></script>
    </body>
</html>