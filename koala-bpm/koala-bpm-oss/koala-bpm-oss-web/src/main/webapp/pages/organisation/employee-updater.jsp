<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade employee-management" id="userModify">
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">修改员工基本信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group row">
                    	<div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">编号:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" placeholder="编号" id="employee-sn"><span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">姓名:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control"  placeholder="姓名" id="employee-name"><span class="required">*</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">性别:</label>
                            <div class="col-lg-8">
                                <div data-role="condition" class="btn-group select" id="employee-gender">
                                </div>
                                <span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">身份证号:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" placeholder="身份证号" id="employee-idNumber"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">手机号码:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" placeholder="手机" id="employee-mobilePhone">
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">家庭电话:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" placeholder="家庭电话" id="employee-familyPhone">
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">邮箱:</label>
                            <div class="col-lg-8">
                                <input type="Email" class="form-control" placeholder="邮箱" id="employee-email">
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">入职时间:</label>
                            <div class="col-lg-8">
                                <div class="input-group date form_datetime" id="employee-entryDate">
                                    <input class="form-control" size="16" type="text" value="" style="width:100%;">
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
                                </div>
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