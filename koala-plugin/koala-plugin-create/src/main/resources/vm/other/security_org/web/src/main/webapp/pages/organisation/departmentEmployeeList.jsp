<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade employeeList">
	<style>
		.employeeList .grid-table-body {
			height: 280px;
		}
		.employeeList .queryAllChildren .checker {
			display: inline;
			left: 8px;
		    position: relative;
		    top: 3px;
		}
		.employeeList .modal-body {
			min-height: 400px;
			padding-bottom: 0;
		}
	</style>
    <div class="modal-dialog" style="width:960px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" >&times;</button>
                <h4 class="modal-title">员工列表</h4>
            </div>
            <div class="modal-body">
               	<div id="employeeList"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-danger" id="deleteRelation">解除关系</button>
            </div>
        </div>
    </div>
</div>