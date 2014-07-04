<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="<c:url value='/js/security/menu.js' />"></script>
<script>
	$(function(){
		function initEditDialog(data, item, grid) {
			dialog = $(data);
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改权限信息' : '添加权限');
			
			var form = dialog.find(".permisstion_form");
			validate(form, dialog, item);
			
			if(item){
				form.find("input[name='permissionName']").val(item.permissionName);
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
					name:"permissionName",	
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
	            	console.log(item);
	            	/**
	            	 * result是表单验证的结果。
	            	 * 如果表单的验证结果为true,说明全部校验都通过，你可以通过ajax提交表单参数
	            	 */
	            	if(result){
	            		var data = form.serialize();
	            		var url = baseUrl + 'add.koala';
	        			if (item) {
	        				url = baseUrl + 'update.koala';
	        				data += ("&permissionId=" + item.permissionId);
	        			}
	        			
	        			$.ajax({
	        				url : url,
	        				data: data,
	        				type: "post",
	        				dataType:"json",
	        				success:function(data){
	        					if (data.result == 'success') {
		        					dialog.trigger('complete');
		        				} else {
		        					dialog.find('.modal-content').message({
		        						type : 'error',
		        						content : data.actionError
		        					});
		        					refreshToken(dialog.find('input[name="koala.token"]'));
		        				}
		        				dialog.find('#save').removeAttr('disabled');
	        				}
	        			});
					}
	            }
	       	});
		};
		
		deletePermission = function(urls, grid) {
			$.ajax({
			    headers: { 
			        'Accept'		: 'application/json',
			        'Content-Type'	: 'application/json' 
			    },
			    'type'		: "Post",
			    'url'		: baseUrl + 'terminate.koala',
			    'data' 		: JSON.stringify(urls),
			    'dataType'	: 'json'
			 }).done(function(data){
			 	if (data.result == 'success') {
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
		
		var role = $('.tab-pane.active').data();
		var roleId = role.roleId ? role.roleId : null;
		
		var columns = [{
				title : "菜单名称",
				name : "name",
				width : 150
			},{
				title : "菜单图片",
				name : "icon",
				width : 150,
				render: function(item, name, index){
					return '<span class="'+item[name]+'"></span>';
				}
			},{
				title : "菜单url",
				name : "url",
				width : 150
			},{
				title : "菜单标识",
				name : "identifier",
				width : 150
			},{
				title : "菜单描述",
				name : "desc",
				width : 150
			}];
		
		var url;
		if(roleId === null){
			url = contextPath + "/auth/menu/findAllMenusTree.koala";
		} else {
			url = contextPath + "/auth/role/pagingQueryGrantUrlAccessResourcesByRoleId.koala?roleId="+roleId;
		}
		var buttons = (function(){
			if(roleId === null){
				return [{
					content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>',
					action: 'add'
				},{
					content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>',
					action: 'modify'
				},{
					content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action: 'delete'
				}];
			} else {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配菜单</button>',
					action : 'assignMenu'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeMenuFromRole'
				}];
			}
		})();
		
		$("<div/>").appendTo($("#tabContent>div:last-child")).grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: false,
             url: url,
             tree: {
             	column: 'name'
             }
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
        	"assignMenu" : function(event, data){
				var grid = $(this);
        		$.get(contextPath + '/pages/auth/select-menu.jsp').done(function(data){
        			var dialog = $(data);
        			dialog.find('#save').click(function(){
        				var saveBtn = $(this);
        				var items = dialog.find('.selectMenuGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('#selectMenuGrid').message({
        						type: 'warning',
        						content: '请选择要分配的menu'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');
        				var data = {};
        				var mDTOs = [];
        				data.roleId = roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					var dto = {};
        					dto.id = items[i].id;
        					mDTOs.push(dto);
        				}
        				data.MenuResourceDTOs = mDTOs;
        				
        				$.ajax({
        					 headers : { 
       					        'Accept': 'application/json',
       					        'Content-Type': 'application/json' 
       					    },
        					url 	: contextPath + '/auth/role/grantMenuResources.koala',
        					type 	: "post",
        					dataType : "json",
        					data 	: JSON.stringify(data),
        					success : function(data){
        						grid.message({
           							type: 'success',
           							content: '保存成功'
           						});
           						dialog.modal('hide');
           						grid.grid('refresh');
        					},
        					error:function(data){
            					saveBtn.attr('disabled', 'disabled');	
            					grid.message({
            						type: 'error',
            						content: '保存失败'
            					});
            				}
        				});
        			}).end().modal({
        				keyboard: false
        			}).on({
       					'hidden.bs.modal': function(){
       						$(this).remove();
       					},
       					
       					'shown.bs.modal': function(){ //弹窗初始化完毕后，初始化url选择表格
       						var columns = [{
       							title : "菜单名称",
       							name : "name",
       							width : 150
       						}, {
       							title : "菜单url",
       							name : "url",
       							width : 150
       						}, {
       							title : "菜单描述",
       							name : "description",
       							width : 200
       						}];
       					
        					dialog.find('.selectMenuGrid').grid({
								identity: 'id',
								columns: columns,
								querys: [{title: 'url名称', value: 'roleNameForSearch'}],
								url: contextPath + '/auth/role/findMenuResourceTreeSelectItemByRoleId.koala?roleId='+roleId,
								tree: {
									column: 'name'
								}
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
			}
        });
	});
</script>