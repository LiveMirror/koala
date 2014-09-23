<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@ page import="java.util.Date"%>
<% String formId = "form_" + new Date().getTime();
   String gridId = "grid_" + new Date().getTime();
   String path = request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);
%>
<!-- strat form -->
<form name=<%=formId%> id=<%=formId%> target="_self" class="form-horizontal searchCondition">
<input type="hidden" class="form-control" name="page" value="0">
<input type="hidden"  class="form-control"  name="pagesize" value="10">
<div class="panel" hidden="true" >
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td>
                <div class="form-group">
                    <label class="control-label" style="width:100px;float:left;">页面名称:&nbsp;</label>

                    <div style="margin-left:15px;float:left;">
                        <input name="name" class="form-control" type="text" style="width:180px;"/>
                    </div>
                    <label class="control-label" style="width:100px;float:left;">页面标识:&nbsp;</label>

                    <div style="margin-left:15px;float:left;">
                        <input name="identifier" class="form-control" type="text" style="width:180px;"/>
                    </div>
                    <label class="control-label" style="width:100px;float:left;">页面描述:&nbsp;</label>

                    <div style="margin-left:15px;float:left;">
                        <input name="description" class="form-control" type="text" style="width:180px;"/>
                    </div>
                    <td style="vertical-align: bottom;">
                        <button id="search" type="button" style="position:relative; margin-left:35px; top: -15px"
                                class="btn btn-success glyphicon glyphicon-search"></button>
                    </td>
                </div>
            </td>
        </tr>
    </table>
</div>
</form>
<!-- end form -->
<div data-role="userGrid"></div>
<script>
	$(function(){
		var baseUrl = contextPath + '/auth/page/';
		
		function initEditDialog(data, item, grid) {
			dialog = $(data);
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改页面元素资源信息' : '添加页面元素资源信息');
			
			var form = dialog.find(".page_form");
			validate(form, dialog, item);
			
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
		
		deletePage = function(pageElements, grid) {

            var data = "";
            $.each(pageElements, function(i, pageElement){
                data += ("pageElementResourceIds=" + pageElement.id + "&");
            });
            data = data.substring(0, data.length-1);

			var url = baseUrl + 'terminate.koala';
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
	
		var tabData = $('.tab-pane.active').data();
		var pageId = tabData.pageId;
		console.log(pageId);
		
		var columns = [{
				title : "页面名称",
				name : "name",
				width : 200
			},{
				title : "页面标识",
				name : "identifier",
				width : 250
			},{
				title : "页面描述",
				name : "description",
				width : 150
			}];
		
		
		var buttons = (function(){
			 if(pageId){
				return [{
					
							content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为角色分配页面</button>',
							action : 'assignPageFromRole'
							},{
							content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>为删除角色分配页面</button>',
							action : 'removePageFromRole'
				        }];
				} else {
					 return [{
							content: '<ks:hasSecurityResource identifier="pageElementResourceManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
							action: 'add'
						},{
							content: '<ks:hasSecurityResource identifier="pageElementResourceManagerUpdate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
							action: 'modify'
						},{
							content: '<ks:hasSecurityResource identifier="pageElementResourceManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button></ks:hasSecurityResource>',
							action: 'delete'
						},{
							content: '<ks:hasSecurityResource identifier="pageElementResourceManagerGrantPermission"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-remove"><span>授权权限</button></ks:hasSecurityResource>',
							action: 'permissionAssignForPage'
						},{
							content : '<ks:hasSecurityResource identifier="pageElementResourceManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;查询&nbsp; <span class="caret"></span> </button></ks:hasSecurityResource>',
							action : 'search'
		 				}];
			}
		})();
		
		//add here
		
	    var url = contextPath + "/auth/page/pagingQuery.koala";
		if(pageId){
           var url = contextPath + "/auth/role/pagingQueryGrantPageElementResourcesByRoleId.koala?roleId="+ pageId;		
		}
		$('[data-role="userGrid"]').grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: true,
             dataFilter:function(result){
                return result.data;
             },
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
		                    content: '请选择要撤销的记录'
		            });
		             return;
	            }
	            grid.confirm({
	                content: '确定要撤销所选记录吗?',
	                callBack: function(){
	                	deletePage(data.item, grid);
	                }
	            });
	            
        	},
        	"permissionAssignForPage" : function(event,data){
        		var items 	= data.item;
				var thiz	= $(this);
				if(items.length == 0){
					thiz.message({type : 'warning',content : '请选择一条记录进行操作'});
					return;
				} 
				if(items.length > 1){
					thiz.message({type : 'warning',content : '只能选择一条记录进行操作'});
					return;
				}
				
				var page = items[0];
                   console.log(page.id);
				openTab('/pages/auth/permission-list.jsp', page.name+'的权限管理', 'roleManager_' + page.id, page.id, {pageId : page.id});
        	},
        	"assignPageFromRole" : function(event, data){
				var grid = $(this);
				/*
				获取选中项的信息
				*/
        		$.get(contextPath + '/pages/auth/select-page.jsp').done(function(data){
        			var dialog = $(data);
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
        				
        				var data = "roleId="+ pageId;
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&pageElementResourceIds=" + items[i].id;
        				}
        				
        				$.post(contextPath + '/auth/role/grantPageElementResourcesToRole.koala', data).done(function(data){
        					 if(data.success){
        						grid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						grid.grid('refresh');
        						}else{
        							grid.message({
        								type : 'error',
        								content : data.errorMessage
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
       					
       						dialog.find('#selectPageGrid').grid({
       						 identity: 'id',
       			             columns: columns,
       			             querys: [{title: '页面名称', value: 'roleNameForSearch'}],
       			             dataFilter:function(result){
                                return result.data;
                             },
       			             url: contextPath + '/auth/role/pagingQueryNotGrantPageElementResourcesByRoleId.koala?roleId='+pageId
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
			'search' : function() {						
				$(".panel").slideToggle("slow");						 
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
						var url = contextPath + '/auth/role/terminatePageElementResourcesFromRole.koala';
						var params = "roleId="+pageId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&pageElementResourceIds=" + data.item[i].id);
						}
						console.log(data.item[0].id);
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
		var formId = $("#<%=formId%>");
		formId.find('#search').on('click', function(){
            var params = {};
            formId.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
                 console.log(name+"=="+params[name]);
            });
           $('[data-role="userGrid"]').getGrid().search(params);
        });
});
</script>