<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<!-- strat form -->
<form name=”permissionListForm“ id="${formId}" target="_self" class="form-horizontal searchCondition">
    <input type="hidden" class="form-control" name="page" value="0">
    <input type="hidden" class="form-control" name="pagesize" value="10">

    <div id="permissionManagerQueryDivId" hidden="true">
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <div class="form-group">
                        <label class="control-label" style="width:100px;float:left;">权限名称:&nbsp;</label>

                        <div style="margin-left:15px;float:left;">
                            <input name="name" class="form-control" type="text" style="width:180px;"/>
                        </div>
                        <label class="control-label" style="width:100px;float:left;">菜单标识:&nbsp;</label>

                        <div style="margin-left:15px;float:left;">
                            <input name="identifier" class="form-control" type="text" style="width:180px;"/>
                        </div>
                        <label class="control-label" style="width:100px;float:left;">权限描述:&nbsp;</label>

                        <div style="margin-left:15px;float:left;">
                            <input name="description" class="form-control" type="text" style="width:180px;"/>
                        </div>
                    </div>
                </td>
                <td style="vertical-align: bottom;">
                    <button id="permissionManagerSearch" type="button" style="position:relative; margin-left:35px; top: -15px"
                            class="btn btn-success"><span class="glyphicon glyphicon-search"></span>&nbsp;</button>
                </td>
            </tr>
        </table>
    </div>
</form>
<!-- end form -->
<div data-role="permissionGrid"></div>
<script>
	$(function(){
		var baseUrl = contextPath + '/auth/permission/';
		function initEditDialog(data, item, grid) {
            dialog = $(data);
            var form = dialog.find(".permisstion_form");
            validate(form, dialog, item);
            if(item){
                dialog.find('.modal-header').find('.modal-title').html('修改权限信息');
                form.find("input[name='name']").val(item.name);
                form.find("input[name='identifier']").val(item.identifier).attr('disabled', 'disabled');
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
		        						content : data.errorMessage
		        					});
		        				}
		        				dialog.find('#save').removeAttr('disabled');
	        				}
	        			});
					}
	            }
	       	});
		};
		
		deletePermission = function(permissions, grid) {

			var url = baseUrl + 'terminate.koala';

            var data = "";
            $.each(permissions, function (i, permission) {
                data += ("permissionIds=" + permission.id + "&");
            });
            data = data.substring(0, data.length - 1);
            $.post(url,data).done(function(data){
			 	if (data.success) {
			 		grid.message({
						type : 'success',
						content : '撤销成功'
					});
			 		grid.grid('refresh');
				} else {
					grid.message({
						type : 'error',
						content : data.errorMessage
					});
				}
			}).fail(function(data){
				grid.message({
					type : 'error',
					content : '撤销失败'
				});
			});
		};
		
		var tabData =  $('[data-role="permissionGrid"]').closest('.tab-pane.active').data();
		var userId 	= tabData.userId;
		var menuId = tabData.menuId;
		var pageId = tabData.pageId;
		var urlId = tabData.url_listId;
		var roleId = tabData.permissionsId;
		
		var columns = [{
				title : "权限名称",
				name : "name",
				width : 150
			},{
				title : "权限标识",
				name : "identifier",
				width : 150
			},{
				title : "权限描述",
				name : "description",
				width : 150
			},{
                title : "查看",
                name : "operate",
                width : 200,
                render: function(item, name, index){
                   return '<a href="#" onclick="showUserDetail('+item.id+', \''+item.name+'\')"><span class="glyphicon glyphicon glyphicon-eye-open"></span>&nbsp;详细</a>';
                }
            }];
		
		var buttons = (function(){
			if(menuId){
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
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为URL分配权限</button>',
					action : 'assignPermissionForUrl'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除URL权限</button>',
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
				},{
                    content : '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search" />&nbsp;高级搜索&nbsp;<span class="caret" /></button>',
                    action : 'permissionManagerQuery'
                }];
			}else {
                return [{
                        content: '<ks:hasSecurityResource identifier="permissionManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
                        action: 'add'
                }, {
                        content: '<ks:hasSecurityResource identifier="permissionManagerUpdate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
                        action: 'modify'
                }, {
                        content: '<ks:hasSecurityResource identifier="permissionManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>撤销</button></ks:hasSecurityResource>',
                        action: 'delete'
                }, {
                        content: '<ks:hasSecurityResource identifier="permissionManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索&nbsp; <span class="caret"></span> </button></ks:hasSecurityResource>',
                        action: 'permissionManagerQuery'
                    }];
			}
		})();
		
		var url = contextPath + '/auth/permission/pagingQuery.koala';
		
		
		if(menuId){
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
		
		$('[data-role="permissionGrid"]').grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: true,
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
                $.get(contextPath + '/pages/auth/permission-template.jsp').done(function(dialog){
                    initEditDialog(dialog, data.item[0], grid);
                });
        	},
        	'delete': function(event, data){
        		var indexs = data.data;
	            var grid = $(this);
        		if(indexs.length == 0){
		            grid.message({
		                   type: 'warning',
		                    content: '请选择要撤销的记录'
		            });
		             return;
	            }
	            grid.confirm({
	                content: '确定要撤销所选记录吗?',
	                callBack: function(){
	                	deletePermission(data.item, grid);
	                }
	            });
        	},
			//TODO MENU
			'assignPermissionForMenu': function(event, data){
        		var grid = $(this);
        		$.get(contextPath + '/pages/auth/permission-select.jsp').done(function(data){
        			var dialog = $(data);

                    dialog.find('#search').on('click', function(){
                        var params = {};
                        dialog.find('.form-control').each(function(){
                            var $this = $(this);
                            var name = $this.attr('name');
                            if(name){
                                params[name] = $this.val();
                            }
                        });
                        $('[data-role="selectPermissionGrid"]').getGrid().search(params);
                    });


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
        				data += "&permissionId=" + items[0].id;
        				
        				$.post(contextPath + '/auth/menu/grantPermisssionsToMenuResource.koala', data).done(function(data){
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
        							content: data.errorMessage
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
									content: data.errorMessage
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
        		$.get(contextPath + '/pages/auth/permission-select.jsp').done(function(data){
        			var dialog = $(data);

                    dialog.find('#search').on('click', function(){
                        var params = {};
                        dialog.find('.form-control').each(function(){
                            var $this = $(this);
                            var name = $this.attr('name');
                            if(name){
                                params[name] = $this.val();
                            }
                        });
                        $('[data-role="selectPermissionGrid"]').getGrid().search(params);
                    });

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
        							content: data.errorMessage
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
									content: data.errorMessage
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
        		$.get(contextPath + '/pages/auth/permission-select.jsp').done(function(data){
        			var dialog = $(data);

                    dialog.find('#search').on('click', function(){
                        var params = {};
                        dialog.find('.form-control').each(function(){
                            var $this = $(this);
                            var name = $this.attr('name');
                            if(name){
                                params[name] = $this.val();
                            }
                        });
                        $('[data-role="selectPermissionGrid"]').getGrid().search(params);
                    });

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
        							content: data.errorMessage
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
       							title : "权限标识",
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
			'permissionManagerQuery' : function() {
				$("#permissionManagerQueryDivId").slideToggle("slow");
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
									content: data.errorMessage
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
        		$.get(contextPath + '/pages/auth/permission-select.jsp').done(function(data){
        			var dialog = $(data);

                    dialog.find('#search').on('click', function(){
                        var params = {};
                        dialog.find('.form-control').each(function(){
                            var $this = $(this);
                            var name = $this.attr('name');
                            if(name){
                                params[name] = $this.val();
                            }
                        });
                        $('[data-role="selectPermissionGrid"]').getGrid().search(params);
                    });

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
        							content: data.errorMessage
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
									content: data.errorMessage
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

        var form = $('#'+'${formId}');
        form.find('#permissionManagerSearch').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
           $('[data-role="permissionGrid"]').getGrid().search(params);
        });
});

    /**
     * 显示详细信息
     * @param id
     * @param userName
     */
    var showUserDetail = function(id, name){
        var thiz = $(this);
        var mark = thiz.attr('mark');
        mark = openTab('/pages/auth/permission-detail.jsp', name, mark, id);
        if (mark) {
            thiz.attr("mark", mark);
        }
    };
</script>
