<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade monitor-param">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">修改监控参数</h4>
            </div>
            <div class="modal-body" style="width:85%; margin-right: auto; margin-left: auto; padding-bottom: 5px;">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="monitorType" class="col-sm-3 control-label">监控内容:</label>
                        <div class="col-sm-9">
                            <span style="position: relative; top: 5px;" id="monitorType"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label  class="col-sm-3 control-label">当前状态:</label>
                        <div class="col-sm-9">
                            <div class="radio">
                                    <span class="checked"><input type="radio"
                                                                 style="opacity: 0;" checked="checked" name="status" value="true"></span>
                            </div>
                            <span style="position: relative; top: 8px;">激活</span>
                            &nbsp;&nbsp;&nbsp;
                            <div class="radio">
                                    <span><input type="radio" style="opacity: 0;"
                                                 name="status" value="false"></span>
                            </div>
                            <span style="position: relative; top: 8px;">关闭</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">监控参数:</label>
                        <div class="col-sm-9">
                              <div style="margin-bottom: 5px;">仅监控执行时间超过<input class="form-control time" id="tracetime"/>毫秒的过程</div>
                              <div style="margin-bottom: 5px;">执行时间超过<input class="form-control time" id="tracetimeout"/>毫秒的过程标记为超时</div>
                            <div style="margin-bottom: 8px; margin-top:8px; display: none;">开启堆栈信息跟踪:
                                <div class="radio stack">
                                    <span class="checked"><input type="radio"
                                                                 style="opacity: 0;" checked="checked" name="stack-status" value="true"></span>
                                     </div>
                                    <span>激活</span>
                                    &nbsp;&nbsp;&nbsp;
                                    <div class="radio stack">
                                        <span><input type="radio" style="opacity: 0;"
                                                     name="stack-status" value="false"></span>
                                    </div>
                                     <span>关闭</span>
                            </div>
                            <div style="margin-bottom: 5px;">
                                  <div style="margin-bottom: 2px;">仅监控以下URL(<span class="required">多个用分号";"隔开</span>)</div>
                                  <textarea class="form-control" rows="3" id="includeUrls"></textarea>
                            </div>
                            <div>
                                 <div style="margin-bottom: 2px;">排除以下URL(<span class="required">多个用分号";"隔开</span>)</div>
                                 <textarea class="form-control" rows="3" id="excludeUrls"></textarea>
                            </div>
                            <div style="margin-bottom: 5px;">
                                <div style="margin-bottom: 2px;">监控以下类或者方法(<span class="required">多个用分号";"隔开</span>)</div>
                                <textarea class="form-control" rows="3" id="detect-clazzs"></textarea>
                            </div>
                            <div>
                                <div style="margin-bottom: 2px;">监控以下包(<span class="required">多个用分号";"隔开</span>)</div>
                                <textarea class="form-control" rows="3" id="detect-packages"></textarea>
                            </div>
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