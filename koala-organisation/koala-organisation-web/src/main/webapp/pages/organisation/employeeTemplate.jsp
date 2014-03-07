<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<div class="modal fade employee-management">
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">录入员工信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                	<k:token/>
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
                                <input type="text" class="form-control" id="employee-name"  placeholder="姓名"><span class="required">*</span>
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
                                <input type="text" class="form-control" placeholder="身份证号" id="employee-idNumber">
                            </div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">所属机构:</label>
                            <div class="col-lg-8">
                                <div class="btn-group select" id="select-department">
                                    <button data-toggle="item" class="btn btn-default" type="button">
                                        选择机构
                                    </button>
                                    <button data-toggle="dropdown" class="btn btn-default dropdown-toggle" type="button">
                                        <span class="caret"></span>
                                    </button>
                                    <input type="hidden" data-toggle="value">
                                </div><span class="required">*</span>
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">岗位:</label>
                            <div class="col-lg-8">
                                <div data-role="condition" class="btn-group select" id="employee-post">
                                </div>
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
                    <div class="form-group row">
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">移动电话:</label>
                            <div class="col-lg-8">
                                <input type="Email" class="form-control" placeholder="手机号码" id="employee-mobilePhone">
                            </div>
                        </div>
                        <div class="col-lg-6 form-group">
                            <label class="col-lg-4 control-label">固定电话:</label>
                            <div class="col-lg-8">
                                <input type="text" class="form-control" placeholder="固定电话" id="employee-familyPhone">
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