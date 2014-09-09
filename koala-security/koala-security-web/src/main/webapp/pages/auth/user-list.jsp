<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<%@ page import="java.util.Date"%>
<% String formId = "form_" + new Date().getTime();
   String gridId = "grid_" + new Date().getTime();
   String path = request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);
%>
<!-- strat form -->
<form name=<%=formId%> id=<%=formId%> target="_self" class="form-horizontal searchCondition">
<input type="hidden" name="page" value="0">
<input type="hidden" name="pagesize" value="10">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
          <div class="form-group">
          <label class="control-label" style="width:100px;float:left;">用户名称:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="name" class="form-control" type="text" style="width:180px;" id="nameID"  dataType="Chinese" require="false" />
        </div>
            <label class="control-label" style="width:100px;float:left;">用户帐号:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="userAccount" class="form-control" type="text" style="width:180px;" id="descID"  />
        </div>
            </div>
            <div class ="form-group">
             <label class="control-label" style="width:100px;float:left;">用户描述:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="description" class="form-control" type="text" style="width:180px;"  />
        </div>
           <label class="control-label" style="width:100px;float:left;">用户状态:&nbsp;</label>
           <select style = "width:180px;margin-left:15px;float:left;">
             <option>可用</option>
             <option>挂起</option>
           </select>
            </div>
            </td>
       <td style="vertical-align: bottom;"><button id="search" type="button" style="position:relative; margin-left:35px; top: -15px" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button></td>
  </tr>
</table>	
</form>
<!-- end form -->

<div data-role="userGrid">
</div>
	<script>
	/*
	*多条件查询
	*/var grid;
	var form;
	var _dialog;
	var tabData = $('[data-role="userGrid"]').closest('.tab-pane.active').data();
	var roleId = tabData.roleId;
	$(function (){
	    grid = $("#<%=gridId%>");
	    form = $("#<%=formId%>");
		PageLoader = {
		   //
		   
		    initSearchPanel:function(){
		        	            	                	            	                	            	                	            	                	            	        	     },
		    initGridPanel: function(){
		         var self = this;
		         var url = contextPath + '/auth/user/pagingQuery.koala?disabled=false';
		         var getButtons = function() {
		 			if (roleId) {
		 				return [{
		 					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配</button>',
		 					action : 'assignUserToRole'
		 				}, {
		 					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
		 					action : 'removeUserForRole'
		 				}];
		 			} else {
		 				return [{
		 					content : '<ks:hasSecurityResource identifier="userManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
		 					action : 'add'
		 				}, {
		 					content : '<ks:hasSecurityResource identifier="userManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
		 					action : 'modify'
		 				}, {
		 					content : '<ks:hasSecurityResource identifier="userManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button></ks:hasSecurityResource>',
		 					action : 'delete'
		 				}, {
		 					content : '<ks:hasSecurityResource identifier="userManagerGrantRole"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配角色</button></ks:hasSecurityResource>',
		 					action : 'roleAssign'
		 				}, {
		 					content : '<ks:hasSecurityResource identifier="userManagerGrantPermission"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配权限</button></ks:hasSecurityResource>',
		 					action : 'permissionAssign'
		 				}, {
		 					content : '<ks:hasSecurityResource identifier="userManagerResetPassword"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;重置密码</button></ks:hasSecurityResource>',
		 					action : 'resetPassword'
		 				},{
		 					content : '<ks:hasSecurityResource identifier="userManagerSuspend"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;禁用</button></ks:hasSecurityResource>',
		 					action : 'forbidden'
		 				}];
		 			}
		 		};
		         return $('[data-role="userGrid"]').off().grid({
		                identity:"id",
		                buttons: getButtons(),
		                url:url,
		                dataFilter:function(result){
		                    return result.data;
		                },
		                columns: [{
							title : "用户名称",
							name : "name",
							width : 150
						}, {
							title : "用户帐号",
							name : "userAccount",
							width : 150
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
						}
		                ]
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
						console.log(data.data[0]);
						console.log(data.item[0].userAccount);
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
					},/*
					'assignUserToRole' : function() {
						userManager().assignRole(roleId, $(this));
					},*/
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
						
						userManager().forbidden(data.item[0] , $this);
					}
				});
		    }
		};
		PageLoader.initGridPanel();
		 form.find('#search').on('click', function(){
	            var params = {};
	            form.find('input').each(function(){
	                var $this = $(this);
	                var name = $this.attr('name');
	                console.log(name);
	                if(name){
	                    params[name] = $this.val();
	                }
	            });
	            $('[data-role="userGrid"]').off().getGrid().search(params);
	        });
	});
</script>
