<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade page-server-summry-info">
    <div class="modal-dialog" style="width:98%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">实时监控信息</h4>
            </div>
            <div class="modal-body" style="max-height:380px;margin-right: auto; margin-left: auto; padding-bottom: 5px; overflow-x:hidden!important;">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" style="width:10%;">在线人数:</label>
                        <div class="col-sm-2">
                            <span style="position: relative; top: 5px;" id="activeCount"></span>
                        </div>
                        <label class="col-sm-2 control-label">页面平均响应速度:</label>
                        <div class="col-sm-2">
                            <span style="position: relative; top: 5px;" id="pageAvgResponseTime"></span>
                        </div>
                        <label class="col-sm-2 control-label">耗时最长页面:</label>
                        <div class="col-sm-2">
                            <span style="position: relative; top: 5px;" id="maxAvgTimePage"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" style="width:10%;">调用最多方法:</label>
                        <div class="col-sm-2">
                            <span style="position: relative; top: 5px;" id="mostCallMethod"></span>
                        </div>
                        <label  class="col-sm-2 control-label">耗时最长方法:</label>
                        <div class="col-sm-2">
                            <span style="position: relative; top: 5px;" id="maxAvgTimeMethod"></span>
                        </div>
                        <label class="col-sm-2 control-label">异常率:</label>
                        <div class="col-sm-2">
                            <span class="form-control-static" style="position: relative;float:left; right: 8px;" id="exceptionRateValue">0</span>
                            <div class="progress progress-striped" style="position: relative; top: 5px;">
                                <div class="progress-bar progress-bar-info" id="exceptionRate">
                                    <span class="sr-only" style="position: relative;color:#000000;"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="server-info">
                        <div class="form-group">
                            <div class="title">24小时内存使用情况</div>
                            <div class="chart" id="memchart"></div>
                        </div>
                        <div class="form-group">
                            <div class="title">24小时CPU使用情况</div>
                            <div class="chart" id="cpuchart"></div>
                        </div>
                        <div class="form-group">
                            <div class="title">磁盘使用情况</div>
                            <div class="chart" id="disklist"></div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-info" data-dismiss="modal">返回</button>
            </div>
        </div>
    </div>
</div>