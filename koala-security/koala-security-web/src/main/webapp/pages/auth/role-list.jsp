<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div id="roleGrid"></div>
<script>
	$(function() {
		var tabData = $('#roleGrid').closest('.tab-pane.active').data();
		var userId = tabData.userId;
		var userAccount = tabData.userAccount;
		var columns = [{
			title : "角色名称",
			name : "roleName",
			width : 250
		}, {
			title : "角色描述",
			name : "roleDesc",
			width : 250
		}];
		
		var getButtons = function() {
			if (userId) {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配角色</button>',
					action : 'assignRole'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeRoleForUser'
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
					content : '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-user"><span>用户管理</button>',
					action : 'assignUser'
				},{
					content : '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配url</button>',
					action : 'urlAssign'
				}, {
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th"><span>资源授权</button>',
					action : 'assignResource'
				}];
			}
		};
		var getQuerys = function() {
			if (userId) {
				return [];
			} else {
				return [{
					title : '角色名称',
					value : 'roleNameForSearch'
				}];
			}
		};
		
		var url = contextPath + '/auth/role/pagingquery.koala';
		if (userAccount) {
			url = contextPath + '/auth/role/findRolesByUsername.koala?username=' + userAccount;
		}
		
		$('#roleGrid').off().grid({
			identity : 'id',
			columns : columns,
			buttons : getButtons(),
			querys : getQuerys(),
			url : url
		}).on({
			'add' : function() {
				roleManager().add($(this));
			},
			'modify' : function(event, data) {
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
				roleManager().modify(data.item[0], $(this));
			},
			'delete' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						roleManager().deleteUser(data.item, $this);
					}
				});
			},
			'assignRole' : function() {
				roleManager().assignRole(userId, userAccount, $(this));
			},
			"urlAssign" : function(event, data){
				var items 	= data.item;
				var thiz	= $(this);
				if(items.length == 0){
					thiz.message({type : 'warning',content : '请选择一条记录进行操作'});
					return;
				} else if(items.length > 1){
					thiz.message({type : 'warning',content : '只能选择一条记录进行操作'});
					return;
				}
				
				var role = items[0];
				/*打开url表格*/
				openTab('/pages/auth/url-list.jsp', role.roleName+'的url管理', 'roleManager_' + role.id, role.id, {roleId : role.roleId});
			},
			'removeRoleForUser' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						roleManager().removeRoleForUser(userId, data.item, $this);
					}
				});
			},
			'assignUser' : function(event, data) {
				var item = data.item;
				var $this = $(this);
				if (item.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行操作'
					});
					return;
				}
				if (item.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行操作'
					});
					return;
				}
				roleManager().assignUser(item[0].roleId, item[0].roleName);
			},
			'assignResource' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行操作'
					});
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行操作'
					});
					return;
				}
				roleManager().assignResource($(this), data.data[0]);
			}
		});
	});
</script>