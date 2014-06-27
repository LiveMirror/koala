<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<div class="modal fade select-role">
	<style>
		.select-role .modal-body {
			height: 420px;
		}
		.select-role .grid-table-body {
			height: 250px;
		}
		.select-role .modal-dialog {
			width: 850px;
		}
	</style>
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title">分配角色</h4>
			</div>
			<div class="modal-body" style="padding-left:45px; padding-right:65px;">
				<div id="selectRoleGrid"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-success" id="save">保存</button>
			</div>
		</div>
	</div>
</div>