<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ss3" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala流程系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/jbpm.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />" rel="stylesheet">
     <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>Koala流程系统</a>
	        <div class="collapse navbar-collapse navbar-ex1-collapse">
	            <div class="btn-group navbar-right">
	                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                    <i class="glyphicon glyphicon-user"></i>
	                    <span>&nbsp;<ss3:authentication property="principal.username" /></span>
	                    <span class="caret"></span>
	                </button>
	                <ul class="dropdown-menu" id="userManager">
	                    <li data-target="modifyPwd"><a href="#">修改密码</a></li>
	                    <li data-target="loginOut"><a href="#">注销</a></li>
	                </ul>
	            </div>
	        </div>
	    </nav>
	</div>
	<div class="g-body">
	    <div class="col-xs-2 g-sidec">
	        <ul class="nav nav-stacked first-level-menu">
	            <li>
                	<a data-toggle="collapse" href="#businessSupport"><i class="glyphicon glyphicon-plane"></i>&nbsp;&nbsp;业务支撑系统&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="businessSupport" class="second-level-menu in">
	                    <li class="submenu" data-role="openTab" data-target="/pages/businesssupport/processlist.jsp" data-title="发起流程" data-mark="startTask"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;发起流程</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/businesssupport/activeTasks.jsp" data-title="待办任务" data-mark="activeTasks"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;待办任务</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/businesssupport/historyTasks.jsp" data-title="已办任务" data-mark="historyTasks"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;已办任务</a></li>
	                	<li class="submenu" data-role="openTab" data-target="/processform/list.koala" data-title="流程表单" data-mark="formList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;流程表单</a></li>
	                </ul>
	            </li>
	            <li>
                <a data-toggle="collapse" href="#userRight"><i class="glyphicon glyphicon-user"></i>&nbsp;&nbsp;用户角色管理&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="userRight" class="second-level-menu in">
	                    <li class="submenu" data-role="openTab" data-target="/pages/auth/user-list.jsp" data-title="用户管理" data-mark="userList"><a ><i class="glyphicon glyphicon-list-alt"></i>&nbsp;用户管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/auth/role-list.jsp" data-title="角色管理" data-mark="roleList"><a ><i class="glyphicon glyphicon-list-alt"></i>&nbsp;角色管理</a></li>
	                </ul>
	            </li>
	             <li>
                <a data-toggle="collapse" href="#organisation"><i class="glyphicon glyphicon-list-alt"></i>&nbsp;&nbsp;组织子系统&nbsp;<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a>
	                <ul id="organisation" class="second-level-menu in">
	                    <li class="submenu" data-role="openTab" data-target="/pages/organisation/departmentList.jsp" data-title="机构管理" data-mark="departmentList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;机构管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/organisation/jobList.jsp" data-title="职务管理" data-mark="jobList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;职务管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/organisation/positionList.jsp" data-title="岗位管理" data-mark="positionList" ><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;岗位管理</a></li>
	                    <li class="submenu" data-role="openTab" data-target="/pages/organisation/employeeList.jsp" data-title="员工管理" data-mark="employeeList"><a><i class="glyphicon glyphicon-list-alt"></i>&nbsp;员工管理</a></li>
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
	    <span>Copyright © 2011-2013 Koala</span>
	</div>
	<script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/lib/koala-tree.js' />?time=<%=time%>" ></script>
	<script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>" ></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/js/processform/form.render.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/validate.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>" ></script>
	<script type="text/javascript" src="<c:url value='/js/auth/role.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/js/auth/user.js' />?time=<%=time%>" ></script>
</body>
</html>