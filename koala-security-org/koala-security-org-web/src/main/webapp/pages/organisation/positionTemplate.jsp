<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="departmentAdd">
    <style>
        .modal-dialog .col-lg-8 > input, .col-lg-8 > span {
            display: inline;
        }
        .modal-dialog .col-lg-8 .form-control {
            width: 85%;
        }
    </style>
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">岗位</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-lg-4 control-label">岗位编号:</label>
                        <div class="col-lg-8">
                            <input type="text" class="form-control" id="positionSn"><span class="required">*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-4 control-label">岗位名称:</label>
                        <div class="col-lg-8">
                            <input type="text" class="form-control" id="positionName"><span class="required">*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-4 control-label">选择机构:</label>
                        <div class="col-lg-8">
                            <div class="btn-group select" id="positionDepartment">
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
                    <div class="form-group">
                        <label class="col-lg-4 control-label">选择职务:</label>
                        <div class="col-lg-8">
                            <div class="btn-group select" id="positionJob"></div><span class="required">*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-4 control-label">是否机构负责岗位:</label>
                        <div class="col-lg-8">
                                <div class="radio"><span ><input value=true type="radio" name="organizationPrincipal"  style="opacity: 0;"></span></div><span style="position: relative; top: 5px;">是</span>
                                &nbsp;&nbsp;&nbsp;<div class="radio"><span class="checked"><input type="radio" checked="checked" value=false name="organizationPrincipal" style="opacity: 0;"></span></div><span style="position: relative; top: 5px;">否</span>
                       </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-4 control-label">描述:</label>
                        <div class="col-lg-8">
                            <input type="text" class="form-control" id="positionDescription">
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