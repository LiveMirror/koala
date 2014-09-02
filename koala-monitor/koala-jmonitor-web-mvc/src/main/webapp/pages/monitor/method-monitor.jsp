<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="form-horizontal method-monitor" role="form" id="methodMonitor">
    <div class="form-group">
        <label class="col-sm-2 control-label" style="width:12%;">监控节点:</label>
        <div class="col-sm-2" style="width:20%;">
            <div class="btn-group select" id="methodMonitorNode"></div>
        </div>
        <label class="col-sm-2 control-label" style="width:9%;">统计内容:</label>
        <div class="col-sm-2" style="width:20%;">
            <div class="btn-group select" id="methodCountType"></div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-1 control-label" style="width:12%;">开始时间:</label>
        <div class="col-sm-2" style="width:24%;">
            <div id="startTime" class="input-group date form_datetime">
                <input class="form-control" size="16" type="text" value="">
                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
            </div>
        </div>
        <label class="col-sm-1 control-label" style="width:9%;">结束时间:</label>
        <div class="col-sm-2" style="width:24%;">
            <div id="endTime" class="input-group date form_datetime">
                <input class="form-control" size="16" type="text" value="">
                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
            </div>
        </div>
        <div class="col-sm-2">
            <button class="btn btn-info" type="button" id="methodSearch"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
        </div>
    </div>
    <div class="form-group" style="width: 98%;margin-left: auto; margin-right: auto; min-height: 10px;">
        <div id="methodDetailChart"></div>
    </div>
    <div class="form-group" style="width: 98%;margin-left: auto; margin-right: auto;">
        <div id="methodDetailGrid"></div>
    </div>
</form>
<script type="text/javascript" src="<c:url value='/js/monitor/methodMonitor.js' />"></script>