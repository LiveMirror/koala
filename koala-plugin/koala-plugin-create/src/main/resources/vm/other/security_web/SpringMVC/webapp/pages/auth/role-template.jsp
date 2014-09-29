<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">添加角色</h4>
			</div>
			<div class="modal-body" style="padding-left:45px; padding-right:65px;">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-lg-3 control-label">角色名称:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="roleName" name = "name">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">角色描述:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="roleDescript" name ="description">
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