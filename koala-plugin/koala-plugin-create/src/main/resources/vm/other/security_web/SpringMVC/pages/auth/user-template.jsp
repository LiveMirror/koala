<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<div class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">添加用户</h4>
			</div>
			<div class="modal-body" style="padding-left:45px; padding-right:65px;">
				<form class="form-horizontal" role="form">
					<k:token/>
					<div class="form-group">
						<label class="col-lg-3 control-label">用户名称:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="userName">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">用户账号:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="userAccount">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">用户邮箱:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="email">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label for="userPassword" class="col-lg-3 control-label">用户密码:</label>
						<div class="col-lg-9">
							<input type="password" class="form-control" id="userPassword">
							<span class="required">*</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">是否启用:</label>
						<div class="col-lg-9">
							<div class="radio">
								<span class="checked">
									<input type="radio"
									style="opacity: 0;" checked="checked" name="isEnable" value="true">
								</span>
							</div>
							<span style="position: relative; top: 8px;">是</span>
							&nbsp;&nbsp;&nbsp;
							<div class="radio">
								<span>
									<input type="radio" style="opacity: 0;"
									name="isEnable" value="false">
								</span>
							</div>
							<span style="position: relative; top: 8px;">否</span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">描述:</label>
						<div class="col-lg-9">
							<input type="text" class="form-control" id="userDescript">
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