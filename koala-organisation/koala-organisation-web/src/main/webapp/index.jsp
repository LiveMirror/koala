<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>组织系统</title>
    <meta charset="utf-8">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/organisation.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />" rel="stylesheet">
    <script>
        var contextPath = '<%=request.getContextPath()%>';
    </script>
</head>
<body>
<div class="g-head">
    <nav class="navbar navbar-default">
        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>组织系统</a>
    </nav>
</div>
<div class="g-body">
    <div class="col-xs-2 g-sidec">
        <ul class="nav nav-stacked first-level-menu">
            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-home"></i>&nbsp;主菜单&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
                <ul id="userRight" class="second-level-menu in">
                   <li class="submenu" data-role="openTab" data-target="/pages/organisation/department-list.jsp" openTree=true data-title="机构管理" data-mark="departmentList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;机构管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="/pages/organisation/job-list.jsp" data-title="职务管理" data-mark="jobList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;职务管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="/pages/organisation/position-list.jsp" data-title="岗位管理" data-mark="positionList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;岗位管理</a></li>
                   <li class="submenu" data-role="openTab" data-target="/pages/organisation/employee-list.jsp" data-title="人员管理" data-mark="employeeList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;人员管理</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <div class="col-xs-10 g-mainc">
        <ul class="nav nav-tabs" id="navTabs">
            <li class="active"><a href="#home" data-toggle="tab">主页</a></li>
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
<script type="text/javascript" src="<c:url value='/lib/koala-tree.js' />?time=<%=time%>"></script>
<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>"></script>
<script type="text/javascript" src="<c:url value='/lib/validate.js' />?time=<%=time%>"></script>
<script>
	$.ajaxSetup({cache:false});
</script>
</body>
</html>