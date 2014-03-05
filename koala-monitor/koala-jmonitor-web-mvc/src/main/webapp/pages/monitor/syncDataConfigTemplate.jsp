<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade">
    <div class="modal-dialog" style="padding-top: 80px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">数据同步配置</h4>
            </div>
            <div class="modal-body" style="width:80%; margin-right: auto; margin-left: auto;">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label  class="col-sm-4 control-label">数据同步服务:</label>
                        <div class="col-sm-8">
                            <div class="radio">
                                    <span class="checked"><input type="radio"
                                                                 style="opacity: 0;" checked="checked" name="schedule_active" value="1"></span>
                            </div>
                            <span style="position: relative; top: 8px;">激活</span>
                            &nbsp;&nbsp;&nbsp;
                            <div class="radio">
                                    <span><input type="radio" style="opacity: 0;"
                                                 name="schedule_active" value="0"></span>
                            </div>
                            <span style="position: relative; top: 8px;">关闭</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="syncInterval" class="col-sm-4 control-label">同步数据间隔:</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="syncInterval" placeholder="输入数字" style="width:45%;display: inline;">
                            <span>&nbsp;秒</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastSyncTime" class="col-sm-4 control-label">上一次同步数据:</label>
                        <div class="col-sm-8">
                           <label id="lastSyncTime"></label>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success" id="save">保存</button>
            </div>
        </div>
    </div>
</div>