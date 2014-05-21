<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<div class="modal fade resource">
	<style>
		.resource .form-control {
			width: 95%;
		}
		.add .parentName{
			display: none;
		}
	</style>
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">添加资源类型</h4>
			</div>
			<div class="modal-body" style="padding-left:45px; padding-right:65px;">
				<form class="form-horizontal" role="form" >
					<k:token/>
					<div class="form-group parentName">
						<label class="col-lg-3 control-label">父资源名称:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="parentName" disabled>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">资源名称:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="name">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">资源标识:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="identifier">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">资源类型:</label>
						<div class="col-lg-9">
							<div class="btn-group select" id="resourceType"></div><span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">资源描述:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="desc">
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					取消
				</button>
				<button type="button" class="btn btn-success" id="save">
					保存
				</button>
			</div>
		</div>
	</div>
</div>