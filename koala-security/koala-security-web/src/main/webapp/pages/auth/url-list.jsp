<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<!-- strat form -->
<form  name="urlListForm" id="${formId}" target="_self" class="form-horizontal searchCondition">
    <input type="hidden" class="form-control" name="page" value="0">
    <input type="hidden" class="form-control" name="pagesize" value="10">

    <div id="urlAccessResourceManagerQueryDivId" hidden="true">
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <div class="form-group">
                        <label class="control-label" style="width:100px;float:left;">URL名称:&nbsp;</label>
                        <div style="margin-left:15px;float:left;">
                            <input name="name" class="form-control" type="text" style="width:180px;"/>
                        </div>

                        <label class="control-label" style="width:100px;float:left;">URL路径:&nbsp;</label>
                        <div style="margin-left:15px;float:left;">
                            <input name="url" class="form-control" type="text" style="width:180px;"/>
                        </div>

                        <label class="control-label" style="width:100px;float:left;">URL描述:&nbsp;</label>
                        <div style="margin-left:15px;float:left;">
                            <input name="description" class="form-control" type="text" style="width:180px;"/>
                        </div>
                    </div>
                </td>
                <td style="vertical-align: bottom;">
                    <button id="urlManagerSearch" type="button" style="position:relative; margin-left:35px; top: -15px"
                            class="btn btn-success"><span class="glyphicon glyphicon-search"></span>&nbsp;</button>
                </td>
            </tr>
        </table>
    </div>
</form>
<!-- end form -->
<div data-role="urlGrid"></div>
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
				form.find("input[name='url']").val(item.url);
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
				},{
					name:"url",
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
		}
		
		deleteUrl = function(urlAccessResources, grid) {

            var data = "";
            $.each(urlAccessResources, function(i, urlAccessResource){
                data += ("urlAccessResourceIds=" + urlAccessResource.id + "&");
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
		
		var role = $('.tab-pane.active').data();
		var roleId = role ? role.roleId : null;
		
		var columns = [{
			title 	: "URL名称",
			name 	: "name",
			width 	: 200
		},{
			title 	: "URL路径",
			name 	: "url",
			width 	: 400
		},{
			title 	: "URL描述",
			name 	: "description",
			width 	: 200
		}];
		var getButtons = function() {
			if (roleId) {
				return [{
					content : '<ks:hasSecurityResource identifier="roleManagerGrantUrlAccessResource"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"/>&nbsp;分配URL</button></ks:hasSecurityResource>',
					action : 'assignUrl'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove" />&nbsp;删除</button>',
					action : 'removeUrlFromRole'
				},{
                    content : '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search" />&nbsp;高级搜索&nbsp;<span class="caret" /></button>',
                    action : 'urlAccessResourceManagerQuery'
                }];
			} else {
				return [{
					content : '<ks:hasSecurityResource identifier="urlAccessResourceManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
					action : 'add'
				}, {
					content : '<ks:hasSecurityResource identifier="urlAccessResourceManagerUpdate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
					action : 'modify'
				}, {
					content : '<ks:hasSecurityResource identifier="urlAccessResourceManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button></ks:hasSecurityResource>',
					action : 'delete'
				},{
					content: '<ks:hasSecurityResource identifier="urlAccessResourceManagerGrantPermission"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-remove"><span>授权权限</button></ks:hasSecurityResource>',
					action: 'permissionAssign'
				},{
					content : '<ks:hasSecurityResource identifier="urlAccessResourceManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索&nbsp; <span class="caret"></span> </button></ks:hasSecurityResource>',
                    action : 'urlAccessResourceManagerQuery'
 				}];
			}
		};
		
		var url;
		if(roleId){
			url = contextPath + '/auth/role/pagingQueryGrantUrlAccessResourcesByRoleId.koala' + '?roleId=' + roleId;
		} else {
			url = contextPath + '/auth/url/pagingQuery.koala';
		}
		
		/*解决id冲突的问题*/
		$('[data-role="urlGrid"]').grid({
			identity : 'id',
			columns : columns,
			buttons : getButtons(),
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
						content : '请选择要撤销的记录'
					});
					return;
				}
				$this.confirm({
					content : '确定要撤销所选记录吗?',
					callBack : function() {
						deleteUrl(data.item, $this);
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
				
				var url_list = items[0];
				openTab('/pages/auth/permission-list.jsp', url_list.name+'的权限管理', 'roleManager_' + url_list.id, url_list.id, {url_listId : url_list.id});
        	},
			"assignUrl" : function(event, data){
				var grid = $(this);
        		$.get(contextPath + '/pages/auth/select-url.jsp').done(function(data){
        			var dialog = $(data);

                    dialog.find('#selectUrlsearch').on('click', function(){
                        var params = {};
                        dialog.find('.form-control').each(function(){
                            var $this = $(this);
                            var name = $this.attr('name');
                            if(name){
                                params[name] = $this.val();
                            }
                        });
                        $('[data-role="selectUrlGrid"]').getGrid().search(params);
                    });

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
        				
        				
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&urlAccessResourceIds="+items[i].id;
        				}
        				
        				$.post(contextPath + '/auth/role/grantUrlAccessResourcesToRole.koala', data).done(function(data){
       						grid.message({
       							type: 'success',
       							content: '保存成功'
       						});
       						dialog.modal('hide');
       						grid.grid('refresh');
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
                                title 	: "URL名称",
                                name 	: "name",
                                width 	: 300
                            },{
                                title 	: "URL路径",
                                name 	: "url",
                                width 	: 300
                            },{
                                title 	: "URL描述",
                                name 	: "description",
                                width 	: 100
                            }];

        					dialog.find('#selectUrlGrid').grid({
        						 identity: 'id',
        			             columns: columns,
                                url: contextPath + '/auth/role/pagingQueryNotGrantUrlAccessResourcesByRoleId.koala?roleId=' + roleId
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
			'urlAccessResourceManagerQuery' : function() {
				$("#urlAccessResourceManagerQueryDivId").slideToggle("slow");
			},
			"removeUrlFromRole" : function(event, data){ //解除授予
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
						var url = contextPath + '/auth/role/terminateUrlAccessResourcesFromRole.koala';
						var params = "roleId="+roleId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&urlAccessResourceIds=" + data.item[i].id);
						}
						
						$.post(url, params).done(function(data) {
							grid.message({
								type : 'success',
								content : '删除成功'
							});
							grid.grid('refresh');
						}).fail(function(data) {
							grid.message({
								type : 'error',
								content : '删除失败'
							});
						});
					}
				});
			}
		});

        var form = $('#'+'${formId}');
        form.find('#urlManagerSearch').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
           $('[data-role="urlGrid"]').getGrid().search(params);
        });

	});
</script>