<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade">
    <div class="modal-dialog dataSource">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加数据源</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-lg-3 control-label">数据源类型</label>
                        <div class="col-lg-9">
                            <div class="btn-group select" id="dataSourceType"></div><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">数据源ID:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourceId"><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataSourceDescription" class="col-lg-3 control-label">描述:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourceDescription">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataSourceJdbcDriver" class="col-lg-3 control-label">JdbcDriver:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourceJdbcDriver"><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataSourceUrl" class="col-lg-3 control-label">Url:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourceUrl" ><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataSourceUserName" class="col-lg-3 control-label">UserName:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourceUserName"><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dataSourcePassword" class="col-lg-3 control-label">Password:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="dataSourcePassword">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3"></label>
                        <div class="col-lg-9">
                            <button class="btn btn-default" id="dataSourceConnectionTest" type="button"><span class="glyphicon glyphicon-transfer"></span>&nbsp;测试连接</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success" id="dataSourceSave">保存</button>
            </div>
        </div>
    </div>
</div>