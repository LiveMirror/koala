<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div class="modal fade menu">
     <style>
        .menu .form-control {
            width: 95%;
        }
    </style>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加菜单</h4>
            </div>
            <div class="modal-body" style="padding-left:45px; padding-right:65px; height: 350px;">
                <form class="form-horizontal menu_form">
                    <div class="form-group parentName">
                        <label class="col-lg-3 control-label">父菜单名称:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" id="parentName" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="id">菜单名称:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" name="name"><span class="required">*</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label" for="identifier">菜单标识:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" name="identifier"><span class="required">*</span>
                        </div>
                    </div>
                     <div class="form-group">
                        <label class="col-lg-3 control-label" for="menuUrl">菜单url:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" name="menuUrl"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">菜单图片:</label>
                        <div class="col-lg-9">
                            <span id="menuIcon" name="menuIcon" class="menu-icon "></span>
                            <button id="iconBtn" type="button" class="btn btn-info">浏览图片</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-3 control-label">资源描述:</label>
                        <div class="col-lg-9">
                            <input type="text" class="form-control" name="description"/>
                        </div>
                    </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		                <button type="button" class="btn btn-success save">保存</button>
		            </div>
                </form>
            </div>
        </div>
    </div>
</div>