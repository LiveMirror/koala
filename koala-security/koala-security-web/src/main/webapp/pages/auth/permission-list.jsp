<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ks" uri="http://www.openkoala.org/security" %>

<link rel="stylesheet" href="../lib/validateForm/css/style.css"/>
<script src="../lib/validateForm/validateForm.js"></script>
<script>
	$(function(){
		var baseUrl = contextPath + '/auth/permission/';
		function initEditDialog(data, item, grid) {
			dialog = $(data);
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改权限信息' : '添加权限');		
			var form = dialog.find(".permisstion_form");
			validate(form, dialog, item);
			console.log(item);
			if(item){
				form.find("input[name='name']").val(item.name);
				form.find("input[name='identifier']").val(item.identifier);
				form.find("input[name='description']").val(item.description);
			}
			
			dialog.modal({
				keyboard : false
			}).on({
				'hidden.bs.modal' : function() {
					$(this).remove();
				},
				'complete' : function() {
					grid.message({
						type : 'success',
						content : '保存成功'
					});
					$(this).modal('hide');
					grid.grid('refresh');
				}
			});
		};
		
		function validate(form, dialog, item){
			var rules = {
				"notnull"		: {
					"rule" : function(value, formData){
						return value ? true : false;
					},
					"tip" : "不能为空"
				}
			};
			
			var inputs = [{ 
					name:"name",	
					rules:["notnull"],
					focusMsg:'必填',	
					rightMsg:"正确"
				}
			];
			
			form.validateForm({
	            inputs		: inputs,
	            button		: ".save",
	            rules 		: rules,
	            onButtonClick:function(result, button, form){
	            	
	            	/**
	            	 * result是表单验证的结果。
	            	 * 如果表单的验证结果为true,说明全部校验都通过，你可以通过ajax提交表单参数
	            	 */
	            	if(result){
	            		var data = form.serialize();
	            		console.log(data);
	            		var url = baseUrl + 'add.koala';
	        			if (item) {
	        				url = baseUrl + 'update.koala';
	        				data += ("&id=" + item.id);
	        			}
	        			
	        			$.ajax({
	        				url : url,
	        				data: data,
	        				type: "POST",
	        				dataType:"json",
	        				success:function(data){
	        					if (data.success) {
		        					dialog.trigger('complete');
		        				} else {
		        					dialog.find('.modal-content').message({
		        						type : 'error',
		        						content : data.actionError
		        					});
		        				}
		        				dialog.find('#save').removeAttr('disabled');
	        				}
	        			});
					}
	            }
	       	});
		};
		
		deletePermission = function(urls, grid) {
			var url = baseUrl + 'terminate.koala';
			console.log(urls[0].id);
			$.post(url,{"permissionIds":urls[0].id}).done(function(data){
			 	if (data.success) {
			 		grid.message({
						type : 'success',
						content : '删除成功'
					});
			 		grid.grid('refresh');
				} else {
					grid.message({
						type : 'error',
						content : data.actionError
					});
				}
			}).fail(function(data){
				grid.message({
					type : 'error',
					content : '删除失败'
				});
			});
		};
		
		var tabData = $('.tab-pane.active').data();
		var userId 	= tabData.userId;
		var menuId = tabData.menuId;
		var pageId = tabData.pageId;
		var urlId = tabData.url_listId;
		var roleId = tabData.permissionsId;
		
	     console.log(roleId);
	     
		var columns = [{
				title : "权限名称",
				name : "name",
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
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为用户分配权限</button>',
					action : 'assignPermissionForUser'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除用户权限</button>',
					action : 'removePermissionForUser'
				}];
			} else if(menuId){
				//@TODO
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为菜单分配权限</button>',
					action : 'assignPermissionForMenu'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除菜单权限</button>',
					action : 'removePermissionForMenu'
				}];
			} else if(pageId){
				//@TODO
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为页面分配权限</button>',
					action : 'assignPermissionForPage'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除页面权限</button>',
					action : 'removePermissionForPage'
				}];
			} else if(urlId){
				//@TODO
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为url分配权限</button>',
					action : 'assignPermissionForUrl'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除url权限</button>',
					action : 'removePermissionForUrl'
				}];
			}else if(roleId){
				//@TODO
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为角色分配权限</button>',
					action : 'assignPermissionForRole'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除角色权限</button>',
					action : 'removePermissionForRole'
				}];
			}else {
				return [
					{content: '<ks:hasSecurityResource identifier="permissionManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>', action: 'add'},
					{content: '<ks:hasSecurityResource identifier="permissionManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>', action: 'modify'},
					{content: '<ks:hasSecurityResource identifier="permissionManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button></ks:hasSecurityResource>', action: 'delete'}
				
				];
			}
		})();
		
		var url = contextPath + '/auth/permission/pagingQuery.koala';
		
		
		if (userId) {
			url = contextPath + '/auth/user/pagingQueryGrantPermissionByUserId.koala?userId=' + userId;
		} else if(menuId){
			//@TODO
			url = contextPath + '/auth/menu/pagingQueryGrantPermissionsByMenuResourceId.koala?menuResourceId=' + menuId;
		} else if(pageId){
			//@TODO
			url = contextPath + '/auth/page/pagingQueryGrantPermissionsByPageElementResourceId.koala?pageElementResourceId=' + pageId;
		} else if(urlId){
			//@TODO
			url = contextPath + '/auth/url/pagingQueryGrantPermissionsByUrlAccessResourceId.koala?urlAccessResourceId=' + urlId;
		}else if(roleId){
			//@TODO
			url = contextPath + '/auth/role/pagingQueryGrantPermissionsByRoleId.koala?roleId=' + roleId;
		}
		
		$("<div/>").appendTo($("#tabContent>div:last-child")).grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: true,
             dataFilter:function(result){
                return result.data;
             },
             url:url
        }).on({
        	'add' : function(event, item) {
				var thiz = $(this);
				$.get(contextPath + '/pages/auth/permission-template.jsp').done(function(data) {
					initEditDialog(data, null, thiz);
				});
			},
        	'modify': function(event, data){
        		var indexs = data.data;
	            var grid = $(this);
	            if(indexs.length == 0){
	                grid.message({
	                    type: 'warning',
	                    content: '请选择一条记录进行修改'
	                });
	                return;
	            }
	            if(indexs.length > 1){
	                grid.message({
	                    type: 'warning',
	                    content: '只能选择一条记录进行修改'
	                });
	                return;
	            }
	            $.get(contextPath + '/pages/auth/permission-template.jsp').done(function(dialog) {
					initEditDialog(dialog, data.item[0], grid);
				});
        	},
        	'delete': function(event, data){
        		var indexs = data.data;
	            var grid = $(this);
        		if(indexs.length == 0){
		            grid.message({
		                   type: 'warning',
		                    content: '请选择要删除的记录'
		            });
		             return;
	            }
	            grid.confirm({
	                content: '确定要删除所选记录吗?',
	                callBack: function(){
	                	deletePermission(data.item, grid);
	                }
	            });
        	},
        	'assignPermissionForUser' : function(event, data){
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
        					data += "&permissionIds=" + items[i].id;
        				}
        				
        				$.post(contextPath + '/auth/user/grantPermissionsToUser.koala', data).done(function(data){
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						saveBtn.attr('disabled', 'disabled');	
        						grid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					grid.message({
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
       							name : "name",
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
       					
        					dialog.find('.selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'roleNameForSearch'}],
        			             dataFilter:function(result){
                                    return result.data;
                                 },
        			             url: contextPath + '/auth/user/pagingQueryNotGrantPermissions.koala?userId='+userId
        			        });        						
       					},
       					'complete': function(){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						grid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	},
        	'removePermissionForUser' : function(event, data) {
				var indexs = data.data;
				var grid = $(this);
				if (indexs.length == 0) {
					grid.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				grid.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						var url = contextPath + '/auth/user/terminatePermissionsByUser.koala';
						var params = "userId="+userId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&permissionIds=" + data.item[i].id);
						}
						
						$.post(url, params).done(function(data){
							if(data.success){
								grid.message({
									type: 'success',
									content: '删除成功'
								});
								grid.grid('refresh');
							}else{
								grid.message({
									type: 'error',
									content: data.actionError
								});
							}
						}).fail(function(data){
							grid.message({
								type: 'error',
								content: '删除失败'
							});
						});
					}
				});
			},
			//TODO MENU
			'assignPermissionForMenu': function(event, data){
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
        				if(items.length > 1){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '只能选择一条记录进行操作'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');
        				
        				var data = "menuResourceId="+menuId;
        				
        					data += "&permissionId=" + items[0].permissionId;
        				
        				
        				$.post(contextPath + '/auth/menu/grantPermisssionsToMenuResource.koala', data).done(function(data){
        					
        					//console.log(data.success);
        					
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						saveBtn.removeAttr('disabled');	
        						grid.message({
        							type: 'error',
        							content: '保存失败'
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					grid.message({
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
       							name : "name",
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
       					
       					
        					dialog.find('.selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'permissionNameForSearch'}],
        			             //url: contextPath + '/auth/permission/pagingquery.koala?menuResourceId='+menuId
        			            url:contextPath + '/auth/menu/pagingQueryNotGrantPermissionsByMenuResourceId.koala?menuResourceId=' + menuId
        			        });        						
       					},
       					'complete': function(){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						grid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	},
        	'removePermissionForMenu':function(event, data) {
				var indexs = data.data;
				var grid = $(this);
				if (indexs.length == 0) {
					grid.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				if (indexs.length > 1) {
					grid.message({
						type : 'warning',
						content : '只能删除一条记录'
					});
					return;
				}
				grid.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						var url = contextPath + '/auth/menu/terminatePermissionsFromMenuResource.koala';
						var params = "menuResourceId="+menuId;
						
							params += ("&permissionId=" + data.item[0].permissionId);
					
						
						$.post(url, params).done(function(data){
							if(data.success){
								grid.message({
									type: 'success',
									content: '删除成功'
								});
								grid.grid('refresh');
							}else{
								grid.message({
									type: 'error',
									content: data.actionError
								});
							}
						}).fail(function(data){
							grid.message({
								type: 'error',
								content: '删除失败'
							});
						});
					}
				});
			},
			//TODO Page
			'assignPermissionForPage': function(event, data){
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
        				if(items.length > 1){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '只能分配一条权限'
        					});
        					return;
        				}
        				saveBtn.attr('disabled', 'disabled');
        				
        				var data = "pageElementResourceId="+pageId;
        					data += "&permissionId=" + items[0].id;
        				
        				$.post(contextPath + '/auth/page/grantPermisssionsToPageElementResource.koala', data).done(function(data){
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						saveBtn.attr('disabled', 'disabled');	
        						grid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					grid.message({
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
       							name : "name",
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
       					
        					dialog.find('.selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'roleNameForSearch'}],
                                 dataFilter:function(result){
                                     return result.data;
                                 },
        			             url: contextPath + '/auth/page/pagingQueryNotGrantPermissionsByPageElementResourceId.koala?pageElementResourceId='+pageId
        			        });        						
       					},
       					'complete': function(){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						grid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	},
        	'removePermissionForPage':function(event, data) {
				var indexs = data.data;
				var grid = $(this);
				if (indexs.length == 0) {
					grid.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				if (indexs.length > 1) {
					grid.message({
						type : 'warning',
						content : '只能删除一条记录'
					});
					return;
				}
				grid.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						var url = contextPath + '/auth/page/terminatePermissionsFromPageElementResource.koala';
						console.info(data.item[0]);
						var params = "permissionId=" + data.item[0].id;
                        params += ("&pageElementResourceId="+pageId);
						console.info(params);
						$.post(url, params).done(function(data){
							if(data.success){
								grid.message({
									type: 'success',
									content: '删除成功'
								});
								grid.grid('refresh');
							}else{
								grid.message({
									type: 'error',
									content: data.actionError
								});
							}
						}).fail(function(data){
							grid.message({
								type: 'error',
								content: '删除失败'
							});
						});
					}
				});
			}, 
			//TODO Url
			'assignPermissionForUrl': function(event, data){
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
        				if(items.length > 1){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '只能分配一条权限'
        					});
        					return;
        				}
        				saveBtn.attr('disabled', 'disabled');
        				
        				var data = "urlAccessResourceId="+urlId;
        					data += "&permissionId=" + items[0].id;
        				
        				$.post(contextPath + '/auth/url/grantPermisssionsToUrlAccessResource.koala', data).done(function(data){
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						saveBtn.attr('disabled', 'disabled');	
        						grid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					grid.message({
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
       							name : "name",
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
       					
        					dialog.find('.selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'roleNameForSearch'}],
        			             dataFilter:function(result){
                                   return result.data;
                                 },
        			             url: contextPath + '/auth/url/pagingQueryNotGrantPermissionsByUrlAccessResourceId.koala?urlAccessResourceId='+urlId
        			        });        						
       					},
       					'complete': function(){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						grid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	},
        	'removePermissionForUrl':function(event, data) {
				var indexs = data.data;
				var grid = $(this);
				if (indexs.length == 0) {
					grid.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				if (indexs.length > 1) {
					grid.message({
						type : 'warning',
						content : '只能删除一条记录'
					});
					return;
				}
				grid.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						var url = contextPath + '/auth/url/terminatePermissionsFromUrlAccessResource.koala';
						console.info(data.item[0]);
						var params = "urlAccessResourceId="+urlId;
							params += ("&permissionId=" + data.item[0].id);
						
						$.post(url, params).done(function(data){
							if(data.success){
								grid.message({
									type: 'success',
									content: '删除成功'
								});
								grid.grid('refresh');
							}else{
								grid.message({
									type: 'error',
									content: data.actionError
								});
							}
						}).fail(function(data){
							grid.message({
								type: 'error',
								content: '删除失败'
							});
						});
					}
				});
			},
			//@TODO
			'assignPermissionForRole':function(event, data){
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
        				if(items.length > 1){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '只能分配一条权限'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');
        				
        				var data = "roleId="+roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&permissionIds=" + items[i].id;
        				}
        				
        				$.post(contextPath + '/auth/role/grantPermissionsToRole.koala', data).done(function(data){
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						saveBtn.attr('disabled', 'disabled');	
        						grid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					saveBtn.attr('disabled', 'disabled');	
        					grid.message({
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
       							name : "name",
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
       					
        					dialog.find('.selectPermissionGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: '权限名称', value: 'roleNameForSearch'}],
        			             dataFilter:function(result){
                                    return result.data;
                                 },
        			             url: contextPath + '/auth/role/pagingQueryNotGrantPermissionsByRoleId.koala?roleId='+roleId
        			        });        						
       					},
       					'complete': function(){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						$(this).modal('hide');
       						grid.grid('refresh');
       					}
        			});
        			 //兼容IE8 IE9
        	        if(window.ActiveXObject){
        	           if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
        	        	   dialog.trigger('shown.bs.modal');
        	           }
        	        }
        		});
        	},
        	'removePermissionForRole':function(event, data) {
				var indexs = data.data;
				var grid = $(this);
				if (indexs.length == 0) {
					grid.message({
						type : 'warning',
						content : '请选择要删除的记录'
					});
					return;
				}
				grid.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						var url = contextPath + '/auth/role/terminatePermissionsFromRole.koala';
						var params = "roleId="+roleId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&permissionIds=" + data.item[i].id);
						}
						
						$.post(url, params).done(function(data){
							if(data.success){
								grid.message({
									type: 'success',
									content: '删除成功'
								});
								grid.grid('refresh');
							}else{
								grid.message({
									type: 'error',
									content: data.actionError
								});
							}
						}).fail(function(data){
							grid.message({
								type: 'error',
								content: '删除失败'
							});
						});
					}
				});
			}
        });
	});
</script>