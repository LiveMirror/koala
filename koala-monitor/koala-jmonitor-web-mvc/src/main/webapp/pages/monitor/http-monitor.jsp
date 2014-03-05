<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form class="form-horizontal http-monitor" id="httpMonitor">
    <div class="form-group">
        <label class="col-sm-2 control-label" style="width:10%;">监控节点:</label>
        <div class="col-sm-2" style="width: 15%;">
            <div class="btn-group select" id="httpMonitorNode"></div>
        </div>
        <label class="col-sm-1 control-label" style="width: 10%;">时间粒度:</label>
        <div class="col-sm-2" style="width: 10%;">
            <div class="btn-group select" id="timeUnit"></div>
        </div>
        <label class="col-sm-1 control-label" style="width: 10%;">选择时间:</label>
        <div class="col-sm-3">
            <div id="time" class="input-group date form_datetime">
                <input class="form-control" size="16" type="text" value="">
                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
            </div>
        </div>
        <div class="col-sm-2">
                <button class="btn btn-info" type="button" id="search"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
        </div>
    </div>
    <div class="form-group" style="width: 98%; margin-left: auto; margin-right: auto;">
        <div id="httpMonitorChart"></div>
    </div>
    <div class="form-group" style="width: 98%; margin-left: auto; margin-right: auto;">
        <div id="httpMonitorGrid"></div>
    </div>
</form>
<script type="text/javascript" src="<c:url value='/js/monitor/httpMonitor.js' />"></script>