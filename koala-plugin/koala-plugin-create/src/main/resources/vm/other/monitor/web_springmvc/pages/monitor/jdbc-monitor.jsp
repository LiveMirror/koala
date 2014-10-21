<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="form-horizontal jdbc-monitor" role="form" id="jdbcMonitor">
    <div class="form-group">
        <label class="col-sm-2 control-label" style="width:10%;">监控节点:</label>
        <div class="col-sm-2">
            <div class="btn-group select" id="dataBaseMonitorNode"></div>
        </div>
        <label class="col-sm-1 control-label">监控项:</label>
        <div class="col-sm-2" style="width:21%;">
            <div class="btn-group select" id="monitorCategory"></div>
        </div>
        <div class="col-sm-4" id="databaseChartSearch" style="width:28%;">
            仅查看时间超过<input class="form-control" style="width:80px; display: inline;" id="timeOut" value="60"/>秒的数据库连接
        </div>
        <div class="col-sm-1">
            <button type="button" class="btn btn-info" id="databaseChartSearchBtn"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
        </div>
    </div>
    <div class="form-group" style="width: 98%; margin-left: auto; margin-right: auto;">
        <div id="databaseChartArea"></div>
    </div>
    <div class="form-group" id="poolMonitor" style="display:none; width: 98%;margin-left: auto; margin-right: auto;">
        <ul class="nav nav-tabs nav-justified">

        </ul>
        <div class="tab-content">

        </div>
    </div>
</form>
<script type="text/javascript" src="<c:url value='/js/monitor/jdbcMonitor.js' />"></script>