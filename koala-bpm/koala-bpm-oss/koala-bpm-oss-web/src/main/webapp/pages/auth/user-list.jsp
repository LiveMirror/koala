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
		}, {
			title : "用户描述",
			name : "userDesc",
			width : 200
		}, {
			title : "是否有效",
			name : "valid",
			width : 100,
			render : function(item, name, index) {
				return item[name] == true ? '<span class="glyphicon glyphicon-ok" style="color:#5CB85C;margin-left:15px;"></span>' : '<span class="glyphicon glyphicon-remove" style="color:#D9534F;margin-left:15px;"></span>';
			}
		}, {
			title : "最后登录时间",
			name : "lastLoginTime",
			width : 150
		}];
		var getButtons = function() {
			if (roleId) {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配用户</button>',
					action : 'assignUser'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeUserForRole'
				}];
			} else {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>',
					action : 'add'
				}, {
					content : '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>',
					action : 'modify'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'delete'
				}, {
					content : '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配角色</button>',
					action : 'roleAssign'
				}, {
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;重置密码</button>',
					action : 'resetPassword'
				}];
			}
		};
		var url = contextPath + '/auth/User/pageJson.koala';
		if (roleId) {
			url += '?roleId=' + roleId;
		}
		$('[data-role="userGrid"]').off().grid({
			identity : 'id',
			columns : columns,
			buttons : getButtons(),
			querys : [{
				title : '用户名称',
				value : 'userNameForSearch'
			}, {
				title : '用户账号',
				value : 'userAccountForSearch'
			}],
			url : url
		}).on({
			'add' : function() {
				userManager().add($(this));
			},
			'modify' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行修改'
					})
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行修改'
					})
					return;
				}
				userManager().modify(data.item[0], $(this));
			},
			'delete' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要删除的记录'
					})
					return;
				}
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						userManager().deleteUser(data.item, $this);
					}
				});
			},
			'roleAssign' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行操作'
					})
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行操作'
					})
					return;
				}
				userManager().assignRole(data.data[0], data.item[0].userAccount);
			},
			'assignUser' : function() {
				userManager().assignUser(roleId, $(this));
			},
			'removeUserForRole' : function(evnet, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要删除的记录'
					})
					return;
				}
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						userManager().removeUserForRole(roleId, data.item, $this);
					}
				});
			},
			'resetPassword' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行修改'
					})
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行修改'
					})
					return;
				}
				userManager().resetPassword(data.item[0], $(this));
			}
		});
	})
</script>