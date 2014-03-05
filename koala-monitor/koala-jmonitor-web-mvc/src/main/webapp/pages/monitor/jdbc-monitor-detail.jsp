<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form class="form-horizontal" role="form">
    <div class="form-group">
        <label class="col-sm-2 control-label">创建时间:</label>
        <div class="col-sm-2">
            <span class="title" id="startTime"></span>
        </div>
        <label class="col-sm-2 control-label">快照时间:</label>
        <div class="col-sm-2">
            <span class="title" id="snapshotTime"></span>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">初始化连接数:</label>
        <div class="col-sm-2">
            <span class="title" id="initConnectionCount"></span>
        </div>
        <label class="col-sm-2 control-label">正在使用连接数:</label>
        <div class="col-sm-2">
            <span class="title" id="activeConnectionCount"></span>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">可用连接数:</label>
        <div class="col-sm-2">
            <span class="title" id="idleConnectionCount"></span>
        </div>
        <label class="col-sm-2 control-label">最大连接数:</label>
        <div class="col-sm-2">
            <span class="title" id="maxConnectionCount"></span>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">最大存活时间:</label>
        <div class="col-sm-2">
            <span class="title" id="maxActiveTime"></span>
        </div>
        <label class="col-sm-2 control-label">连接数比例:</label>
        <div class="col-sm-3">
            <div class="tip">
                <div></div>在用
                <div></div>可用
                <div></div>未创建
            </div>
            <div class="progress">
                <div class="progress-bar progress-bar-danger" id="activeConnectionCountRate">
                    <span class="sr-only"></span>
                </div>
                <div class="progress-bar progress-bar-success" id="idleConnectionCountRate">
                    <span class="sr-only"></span>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group" id="poolDetailGrid" style="width: 98%;margin-left: auto; margin-right: auto;">

    </div>
</form>