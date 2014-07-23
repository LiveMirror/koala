<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>


<link rel="stylesheet" href="../lib/validateForm/css/style.css"/>
<script src="../lib/validateForm/validateForm.js"></script>

<script>
	$(function(){
		var baseUrl = contextPath + '/auth/page/';
		
		function initEditDialog(data, item, grid) {
			dialog = $(data);
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改页面信息' : '添加页面');
			
			var form = dialog.find(".page_form");
			validate(form, dialog, item);
			
			if(item){
				form.find("input[name='name']").val(item.name);
				form.find("input[name='identifier']").val(item.identifier);
				form.find("input[name='pageElementType']").val(item.pageElementType);
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
				"notnull" : {
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
				}];
			
			form.validateForm({
	            inputs		: inputs,
	            button		: ".save",
	            rules 		: rules,
	            onButtonClick:function(result, button, form){
	            	//console.log(item);
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
	        				type: "Get",
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
		
		deletePage = function(urls, grid) {
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
	
	    //add here
	   // var roleId = role.roleId;
		//end add here
		
		
		var columns = [{
				title : "页面名称",
				name : "name",
				width : 150
			},{
				title : "页面类型",
				name : "pageElementType",
				width : 150
			},{
				title : "页面标识",
				name : "identifier",
				width : 150
			},{
				title : "页面描述",
				name : "description",
				width : 150
			}];
		
		
		var buttons = [{
					content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>',
					action: 'add'
				},{
					content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>',
					action: 'modify'
				},{
					content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action: 'delete'
				},{
					content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>授权</button>',
					action: 'permissionAssign'
				}];
	

		
		
		//add here
		
	    var url = contextPath + "/auth/page/pagingQuery.koala";
		$("<div/>").appendTo($("#tabContent>div:last-child")).grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: false,
             url: url
        }).on({
        	'add': function(evnet, item){
	            
        		var thiz = $(this);
				$.get(contextPath + '/pages/auth/page-template.jsp').done(function(data) {
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
	            $.get(contextPath + '/pages/auth/page-template.jsp').done(function(dialog) {
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
	                	deletePage(data.item, grid);
	                }
	            });
	            
        	},
        	"permissionAssign" : function(event,data){
        		var items 	= data.item;
				var thiz	= $(this);
				if(items.length == 0){
					thiz.message({type : 'warning',content : '请选择一条记录进行操作'});
					return;
				} else if(items.length > 1){
					thiz.message({type : 'warning',content : '只能选择一条记录进行操作'});
					return;
				}
				
				var page = items[0];
                   console.log(page);
				openTab('/pages/auth/permission-list.jsp', page.name+'的权限管理', 'roleManager_' + page.id, page.id, {pageId : page.pageId});
        	},
        	"assignPage" : function(event, data){
				var grid = $(this);
				/*
				获取选中项的信息
				*/
				//console.log(grid);
        		$.get(contextPath + '/pages/auth/select-page.jsp').done(function(data){
        			var dialog = $(data);
        			//console.log(dialog);
        			dialog.find('#save').click(function(){
        				var saveBtn = $(this);
        				var items = dialog.find('#selectPageGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('#selectPageGrid').message({
        						type: 'warning',
        						content: '请选择要分配的页面'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');
        				        	
        				
        				
        				var data = "roleId="+roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&id=" + items[i].id;
        				}
        				
        				$.get(contextPath + '/auth/page/grantPermisssionsToPageElementResource.koala', data).done(function(data){
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
       					
       					'shown.bs.modal': function(){ //弹窗初始化完毕后，初始化url选择表格
       						var columns = [{
       							title : "页面名称",
       							name : "name",
       							width : 150
       						},
       					 {
       							title : "页面标识",
       							name : "identifier",
       							width : 150
       						},{
       							title : "页面类型",
       							name : "pageElementType",
       							width : 150
       						}, {
       							title : "页面描述",
       							name : "description",
       							width : 200
       						}];
       					  
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
			'removePageFromRole' : function(event, data) {
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
						var url = contextPath + '/auth/page/terminate.koala';
						var params = "roleId="+roleId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&id=" + data.item[i].id);
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