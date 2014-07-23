<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div data-role="userGrid">
 
   <!--<div class = "searchCondition" data-role="userSearch">
   <div class = "userNameContainer">
   <label for = "userName_Search" >用户名称:</label>
   <input type = "text" class = "input-medium form-control userName_Search" name = "userName_Search" value="qq"/></div>
   <div class = "userStateContainer">
   <label for = "userState">用户状态：</label>
   <select class = "stateSelect">
    <option>请选择</option>
   <option name = "activate">激活</option>
   <option name = "suspend">挂起</option>
   </select></div>
   <div class ="userTelContainer">
   <label for = "userTel">电话：</label>
   <input type="text" class = "input-medium form-control userTel" name = "userTel"/></div>
   <div class = "userSubmitContainer">
   <input type ="button" class = "btn submitValue" value="查询" /></div>
   <style>
    
      .searchCondition{width:100%;margin:0 auto;}
      .userNameContainer,.userStateContainer,.userTelContainer,.userSubmitContainer{float:left;}
      .userNameContainer label,.userStateContainer label,.userTelContainer label{width:65px;float:left;margin:6px;}
      .userNameContainer input,.userTelContainer input{width:165px;height:30px;}
      .userStateContainer,.userTelContainer,.userSubmitContainer{margin-left:5%;}
      
   </style>
</div>-->
</div>
<script>
   
   /* $(function(){
		   var baseUrl = contextPath + '/auth/user/';
		   var userName_Search = $('.userName_Search').val();
		   var userTel = $('.userTel').val();
		   var stateSelect = $('.stateSelect').val();
		   if(stateSelect == '激活'){
			   var stateSelect_ = "activate";	   
		   }else if(stateSelect == '挂起'){
			   var stateSelect_ = "suspend";
		   }
		// var formData = $('[data-role = "userSearch"]').data();//获取class
		// var searchId = formData.roleId;
		 
		 
		 
	 $('.submitValue').click(function(){
		 
		   $.post(baseUrl+'pagingquery.koala?userName_Search='+userName_Search+'&stateSelect_='+stateSelect_+'&userTel='+userTel).done(function(data){
			   $('[data-role="userGrid"]').data("koala.grid").update(data);
		   });
    });
    });*/

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
				return item[name] == true ? '<span class="glyphicon glyphicon-remove" style="color:#D9534F;margin-left:15px;"></span>' : '<span class="glyphicon glyphicon-ok" style="color:#5CB85C;margin-left:15px;"></span>';
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
					content : '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配权限</button>',
					action : 'permissionAssign'
				}, {
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;重置密码</button>',
					action : 'resetPassword'
				},{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;禁用</button>',
					action : 'forbidden'
				}];
			}
		};
		var url = contextPath + '/auth/user/pagingquery.koala?disabled=false';
	    //	if (roleId) {
		//	url += '?roleId=' + roleId;
		//}
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
			}, {
				title : '用户状态',
				value : 'userState'
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
				userManager().modify(data.item[0], $(this));
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
				userManager().assignRole(data.data[0], data.item[0].userAccount);
			},
			"permissionAssign":function(event,data) {
				var items 	= data.item;
				var thiz	= $(this);
				if(items.length == 0){
					thiz.message({type : 'warning',content : '请选择一条记录进行操作'});
					return;
				} else if(items.length > 1){
					thiz.message({type : 'warning',content : '只能选择一条记录进行操作'});
					return;
				}
				
				var user = items[0];
				/*打开权限表格*/
				openTab('/pages/auth/permission-list.jsp', user.userAccount + '的权限管理', 'roleManager_' + user.id, user.id, {userId:user.id});
			},
			'assignUserToRole' : function() {
				userManager().assignRole(roleId, $(this));
			},
			'removeUserForRole' : function(evnet, data) {
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
			
				userManager().resetPassword(data.item[0], $(this));
			},
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
			},
			'forbidden' : function(event, data) {
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
				console.log(data.item[0]);
				userManager().forbidden(data.item[0] , $this);
			}
		});
	});
</script>
