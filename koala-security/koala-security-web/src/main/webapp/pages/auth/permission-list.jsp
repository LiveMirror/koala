<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="permissionListGrid"></div>
<script type="text/javascript" src="<c:url value='/js/security/permission.js' />"></script>
<script>
	$(function(){
		var tabData = $('.tab-pane.active').data();
		var userId = tabData ? tabData.userId : null;
		
		var columns = [{
				title : "权限名称",
				name : "permissionName",
				width : 150
			},{
				title : "菜单标识",
				name : "identifier",
				width : 150
			},{
				title : "权限描述",
				name : "description",
				width : 150
			}];
		
		var buttons = (function(){
			if(userId){
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配权限</button>',
					action : 'assignPermission'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removePermissionForUser'
				}];
			} else {
				return [
					{content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>', action: 'add'},
					{content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>', action: 'modify'},
					{content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>', action: 'delete'}
				];
			}
		})();
		
		$('#permissionListGrid').grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: false,
             url: contextPath + '/auth/permission/pagingquery.koala'
        }).on({
        	'add': function(evnet, data){
        		var indexs = data.data;
	            var $this = $(this);
	            if(indexs.length > 1){
	                $this.message({
	                    type: 'warning',
	                    content: '只能选择一条记录'
	                });
	                return;
	            }
	            if(indexs.length == 1){
	            	var data = data.item[0];
	            	if(data.menuType == '1'){
	            		 $this.message({
	 	                    type: 'warning',
	 	                    content: '菜单下不能新增菜单！'
	 	                });
	 	                return;
	            	}
        			menuManager().add($(this), data);
	            }else{
	            	menuManager().add($(this));
	            }
        	},
        	'modify': function(event, data){
        		var indexs = data.data;
	            var $this = $(this);
	            if(indexs.length == 0){
	                $this.message({
	                    type: 'warning',
	                    content: '请选择一条记录进行修改'
	                });
	                return;
	            }
	            if(indexs.length > 1){
	                $this.message({
	                    type: 'warning',
	                    content: '只能选择一条记录进行修改'
	                });
	                return;
	            }
				menuManager().modify(data.item[0], $(this));
        	},
        	'delete': function(event, data){
        		var indexs = data.data;
	            var $this = $(this);
        		if(indexs.length == 0){
		            $this.message({
		                   type: 'warning',
		                    content: '请选择要删除的记录'
		            });
		             return;
	            }
	            $this.confirm({
	                content: '确定要删除所选记录吗?',
	                callBack: function(){menuManager().deleteItem(data.item, $this);}
	            });
        	},
        	'assignPermission' : function(event, data){
        		var grid = $(this);
        		$.get(contextPath + '/pages/auth/select-permission.jsp').done(function(data){
        			var dialog = $(data);
        			dialog.find('#save').click(function(){
        				var saveBtn = $(this);
        				var items = dialog.find('.selectPermissionGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '请选择要分配的权限'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');	
        				var data = "userId="+userId;
        				
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&permissionIds="+items[i].permissionId;
        				}
        				
        				$.post(contextPath + '/auth/user/grantPermissions.koala', data).done(function(data){
        					if(data.success){
        						dataGrid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						dataGrid.grid('refresh');
        					}else{
        						saveBtn.attr('disabled', 'disabled');	
        						dataGrid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					dataGrid.message({
        						type: 'error',
        						content: '保存失败'
        					});
        				});
        			}).end().modal({
        				keyboard: false
        			}).on({
       					'hidden.bs.modal': function(){
       						$(this).remove();
       					},
       					
       					'shown.bs.modal': function(){ //弹窗初始化完毕后，初始化权限选择表格
       						var columns = [{
       							title : "权限名称",
       							name : "roleName",
       							width : 150
       						},{
       							title : "权限描述",
       							name : "description",
       							width : 150
       						}];
       					
        					dialog.find('#selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'roleNameForSearch'}],
        			             url: contextPath + '/auth/user/pagingQueryNotGrantPermissions.koala?userId='+userId
        			        });        						
       					},
       					'complete': function(){
       						dataGrid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						dataGrid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	}
        });
	});
</script>