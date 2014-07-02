<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<link rel="stylesheet" href="../lib/validateForm/css/style.css"/>
<script src="../lib/validateForm/validateForm.js"></script>
<script>
	$(function() {
		var baseUrl = contextPath + '/auth/url/';
		function initEditDialog(data, item, grid) {
			dialog = $(data);
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改url信息' : '添加url');
			
			var form = dialog.find("#url_form");
			validate(form, dialog, item);
			if(item){
				/*TODO*/
				form.find("input[name='name']").val(item.name);
				form.find("input[name='identifier']").val(item.identifier);
				form.find("input[name='description']").val(item.description);
				form.find("input[name='url']").val(item.url);
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
				},{ 
					name:"identifier",	
					rules:['notnull'],
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
		}
		
		deleteUrl = function(urls, grid) {
			$.ajax({
			    headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
			    'type'	: "Post",
			    'url'	: baseUrl + 'terminate.koala',
			    'data': JSON.stringify(urls),
			    'dataType': 'json'
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
		var roleId = role ? role.roleId : null;
		
		console.log(role);
		
		var columns = [{
			title 	: "url路径",
			name 	: "url",
			width 	: 150
		}, {
			title 	: "url标识",
			name 	: "identifier",
			width 	: 150
		}, {
			title 	: "url描述",
			name 	: "description",
			width 	: 200
		}, {
			title : "是否有效",
			name : "disabled",
			width : 100,
			render : function(item, name, index) {
				return item[name] == true ? 
						'<span class="glyphicon glyphicon-ok" style="color:#5CB85C;margin-left:15px;"></span>' : 
							'<span class="glyphicon glyphicon-remove" style="color:#D9534F;margin-left:15px;"></span>';
			}
		}];
		var getButtons = function() {
			if (roleId) {
				return [{
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配url</button>',
					action : 'assignUrl'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeUrlForUser'
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
				}];
			}
		};
		
		var url;
		if(roleId){
			url = contextPath + '/auth/role/pagingQueryGrantUrlAccessResourcesByRoleId.koala' + '?roleId=' + roleId;
		} else {
			url = contextPath + '/auth/url/pagingquery.koala';
		}
		
		/*解决id冲突的问题*/
		$("<div/>").appendTo($("#tabContent>div:last-child")).grid({
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
			'add' : function(event, item) {
				var thiz = $(this);
				$.get(contextPath + '/pages/auth/url-template.jsp').done(function(data) {
					initEditDialog(data, null, thiz);
				});
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
				
				$.get(contextPath + '/pages/auth/url-template.jsp').done(function(dialog) {
					initEditDialog(dialog, data.item[0], $this);
				});
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
						deleteUrl(data.item, $this);
					}
				});
			},
			"assignUrl" : function(event, data){
				var grid = $(this);
        		$.get(contextPath + '/pages/auth/select-url.jsp').done(function(data){
        			var dialog = $(data);
        			dialog.find('#save').click(function(){
        				var $saveBtn = $(this);
        				var items = dialog.find('#selectUrlGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('#selectUrlGrid').message({
        						type: 'warning',
        						content: '请选择要分配的url'
        					});
        					return;
        				}
        				
        				$saveBtn.attr('disabled', 'disabled');	
        				var data = "roleId="+roleId;
        				
        				console.table(items);
        				
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&urlAccessResourceIds="+items[i].id;
        				}
        				
        				$.post(contextPath + '/auth/role/grantUrlAccessResources.koala', data).done(function(data){
        					if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        					}else{
        						$saveBtn.attr('disabled', 'disabled');	
        						grid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					$saveBtn.attr('disabled', 'disabled');	
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
       					
       					'shown.bs.modal': function(){ //弹窗初始化完毕后，初始化url选择表格
       						var columns = [{
       							title : "url路径",
       							name : "url",
       							width : 150
       						}, {
       							title : "url标识",
       							name : "identifier",
       							width : 150
       						}, {
       							title : "url描述",
       							name : "description",
       							width : 200
       						}];
       					
        					dialog.find('#selectUrlGrid').grid({
        						 identity: 'id',
        			             columns: columns,
        			             querys: [{title: 'url名称', value: 'roleNameForSearch'}],
        			             url: contextPath + '/auth/role/pagingQueryNotGrantUrlAccessResourcesByRoleId.koala?roleId='+roleId
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
			
			"removeUrlForUser" : function(event, data){ //解除授予
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
			}
		});
	});
</script>