<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<div class="modal fade" id="jobEditor">
    <style>
        .modal-dialog  input {
            width: 300px;
        }
        .modal-dialog .modal-body {
            padding-top: 20px;
            padding-left: 40px;
            padding-bottom: 10px;
        }
        .modal-dialog .modal-body .help-block {
            display: none;
            font-weight: normal;
            margin-bottom: 0;
        }
        .modal-dialog .modal-footer {
            margin-top: 0;
        }
        .modal-dialog .col-lg-9 > input, .col-lg-9 > span {
            display: inline;
        }
        .modal-dialog .col-lg-9 .form-control {
            width: 85%;
        }
        .modal-dialog .col-lg-9 > span {
            position: relative;
            top: 3px;
            margin-left: 5px;
            color: red;
        }
        .modal-dialog  .checker {
            padding-top: 5px;
        }
    </style>
    <div class="modal-dialog" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加职位</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                	<k:token/>
                    <div class="form-group">
                        <label for="jobSn" class="col-lg-3 control-label">职位编号:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="jobSn"><span>*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="jobName" class="col-lg-3 control-label">职位名称:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="jobName"><span>*</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="jobDescription" class="col-lg-3 control-label">职位描述:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="jobDescription">
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
