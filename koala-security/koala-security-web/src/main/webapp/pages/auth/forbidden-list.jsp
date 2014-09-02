<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div data-role="userGrid"></div>
<script>
	$(function() {
		var tabData = $('[data-role="userGrid"]').closest('.tab-pane.active').data();
		var roleId = tabData.roleId;
		var columns = [{
			title : "用户名称",
			name : "name",
			width : 150
		}, {
			title : "用户帐号",
			name : "userAccount",
			width : 150
		}, {
			title : "用户邮箱",
			name : "email",
			width : 180
		},{
			title : "联系电话",
			name : "telePhone",
			width : 180
		}, {
			title : "用户描述",
			name : "description",
			width : 200
		}, {
			title : "是否有效",
			name : "valid",
			width : 100,
			render : function(item, name, index) {
				return item[name] != true ? '<span class="glyphicon glyphicon-remove" style="color:#D9534F;margin-left:15px;"></span>' : '<span class="glyphicon glyphicon-ok" style="color:#5CB85C;margin-left:15px;"></span>';
			}
		}];
		
		
		var getButtons = function() {
			if (roleId) {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配用户</button>',
					action : 'assignUserToRole'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeUserForRole'
				}];
			} else {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;激活</button>',
					action : 'available'
				}];
			}
		};
		var url = contextPath + '/auth/user/pagingQuery.koala?disabled=true';
		
		$('[data-role="userGrid"]').off().grid({
			identity : 'id',
			columns : columns,
			buttons : getButtons(),
			dataFilter:function(result){
                return result.data;
            },
			url : url
		}).on({
			'available' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行修改'
					});
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行修改'
					});
					return;
				}
				
				userManager().available(data.item[0],$this);
			}
		});
	});
</script>
