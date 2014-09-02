<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala监控系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/monitor.css' />?time=<%=time%>"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/lib/jqplot/css/jquery.jqplot.css' />"   rel="stylesheet">
    <link href="<c:url value='/lib/jqplot/css/shCoreDefault.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/lib/jqplot/css/shThemejqPlot.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/monitor.css' />"   rel="stylesheet">
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>Koala监控系统</a>
	        <div class="collapse navbar-collapse navbar-ex1-collapse">
	        </div>
	    </nav>
	</div>
	<div class="g-body">
	    <div class="col-xs-2 g-sidec">
	        <ul class="nav nav-stacked first-level-menu">
	            <li>
                	<a data-toggle="collapse" href="#businessSupport"><i class="glyphicon glyphicon-list"></i>&nbsp;监控数据&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="businessSupport" class="second-level-menu in">
	                    <li class="submenu" data-role="openTab" data-target="/pages/monitor/http-monitor.jsp" data-title="HTTP监控" data-mark="httpMonitor"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;HTTP监控</a></li>
                        <li class="submenu" data-role="openTab" data-target="/pages/monitor/method-monitor.jsp" data-title="方法监控" data-mark="methodMonitor"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;方法监控</a></li>
                        <li class="submenu" data-role="openTab" data-target="/pages/monitor/jdbc-monitor.jsp" data-title="数据库监控" data-mark="databaseMonitor"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;数据库监控</a></li>
	                </ul>
	            </li>
	            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-list"></i>&nbsp;监控节点&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="userRight" class="second-level-menu in">
                         <li class="submenu" data-role="openTab" data-target="/pages/monitor/monitor-node-list.jsp" data-title="监控节点列表" data-mark="monitorNodeList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;监控节点列表</a></li>
	                </ul>
	            </li>
	             <li>
                    <a data-toggle="collapse" href="#organisation"><i class="glyphicon glyphicon-list"></i>&nbsp;监控服务&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="organisation" class="second-level-menu in">
	                    <li class="submenu" data-role="openTab" data-target="/pages/monitor/schedule-list.jsp" data-title="定时任务" data-mark="scheduleList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;定时任务</a></li>
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
    <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>"></script>
	<script type="text/javascript" src="<c:url value='/lib/validate.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/excanvas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jquery.jqplot.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/shCore.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/shBrushXml.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.barRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.pieRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.categoryAxisRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.pointLabels.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.dateAxisRenderer.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.highlighter.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/jqplot/js/jqplot.cursor.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>"></script>
</body>
</html>