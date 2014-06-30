<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div id="urlGrid"></div>
<script>
	$(function() {
		var tabData = $('#urlGrid').closest('.tab-pane.active').data();
		var userId = tabData ? tabData.userId : null;
		
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
			if (userId) {
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
		var url = contextPath + '/auth/url/pagingquery.koala';
		
		//roleId ? (url += '?roleId=' + roleId) : "";
		$('#urlGrid').off().grid({
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
			
			"assignUrl" : function(event, data){
        		$.get(contextPath + '/pages/auth/select-url.jsp').done(function(data){
        			var dialog = $(data);
        			dialog.find('#save').click(function(){
        				var $saveBtn = $(this);
        				var items = dialog.find('#selectUrlGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('.modal-content').message({
        						type: 'warning',
        						content: '请选择要分配的url'
        					});
        					return;
        				}
        				
        				$saveBtn.attr('disabled', 'disabled');	
        				var data = "userId="+userId;
        				
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&urlIds="+items[i].urlId;
        				}
        				
        				$.post(contextPath + '/auth/user/grantUrls.koala', data).done(function(data){
        					if(data.success){
        						dataGrid.message({
        							type: 'success',
        							content: '保存成功'
        						});
        						dialog.modal('hide');
        						dataGrid.grid('refresh');
        					}else{
        						$saveBtn.attr('disabled', 'disabled');	
        						dataGrid.message({
        							type: 'error',
        							content: data.actionError
        						});
        					}
        				}).fail(function(data){
        					$saveBtn.attr('disabled', 'disabled');	
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
        			             url: contextPath + '/auth/user/pagingQueryNotGrantUrls.koala?userId='+userId
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