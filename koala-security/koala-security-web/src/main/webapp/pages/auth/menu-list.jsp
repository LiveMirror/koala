<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<script>
	$(function(){

		var baseUrl 	= contextPath + '/auth/menu/';
		function initEditDialog(dialog, item, grid, opreate) {
			dialog = $(dialog);
			dialog.find('.modal-header').find('.modal-title').html( item && opreate =='modify' ? '修改菜单信息' : '添加菜单');
			
			var form = dialog.find(".menu_form");
			validate(form, dialog, item, opreate);
		    //console.log(item[3]);
		   if(item != null){
				form.find("input[name='parentId']").val(item.name);			    	        
			}
			
			if(item && opreate == "modify"){	
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
			
			form.find("#menuIcon").addClass('glyphicon glyphicon-list-alt').attr('src', 'glyphicon glyphicon-list-alt');
			form.find('#iconBtn').on('click', function(){
	            var icons = ['glyphicon  glyphicon-adjust','glyphicon  glyphicon-align-center','glyphicon  glyphicon-align-justify','glyphicon  glyphicon-align-left','glyphicon  glyphicon-align-right','glyphicon  glyphicon-arrow-down','glyphicon  glyphicon-arrow-left','glyphicon  glyphicon-arrow-right','glyphicon  glyphicon-arrow-up','glyphicon  glyphicon-asterisk','glyphicon  glyphicon-backward','glyphicon  glyphicon-ban-circle','glyphicon  glyphicon-barcode','glyphicon  glyphicon-bell','glyphicon  glyphicon-bold','glyphicon  glyphicon-book','glyphicon  glyphicon-bookmark','glyphicon  glyphicon-briefcase','glyphicon  glyphicon-bullhorn','glyphicon  glyphicon-calendar','glyphicon  glyphicon-camera','glyphicon  glyphicon-certificate','glyphicon  glyphicon-check','glyphicon  glyphicon-chevron-down','glyphicon  glyphicon-chevron-left','glyphicon  glyphicon-chevron-right','glyphicon  glyphicon-chevron-up','glyphicon  glyphicon-circle-arrow-down','glyphicon  glyphicon-circle-arrow-left','glyphicon  glyphicon-circle-arrow-right','glyphicon  glyphicon-circle-arrow-up','glyphicon  glyphicon-cloud','glyphicon  glyphicon-cloud-download','glyphicon  glyphicon-cloud-upload','glyphicon  glyphicon-cog','glyphicon  glyphicon-collapse-down','glyphicon  glyphicon-collapse-up','glyphicon  glyphicon-comment','glyphicon  glyphicon-compressed','glyphicon  glyphicon-copyright-mark','glyphicon  glyphicon-credit-card','glyphicon  glyphicon-cutlery','glyphicon  glyphicon-dashboard','glyphicon  glyphicon-download','glyphicon  glyphicon-download-alt','glyphicon  glyphicon-earphone','glyphicon  glyphicon-edit','glyphicon  glyphicon-eject','glyphicon  glyphicon-envelope','glyphicon  glyphicon-euro','glyphicon  glyphicon-exclamation-sign','glyphicon  glyphicon-expand','glyphicon  glyphicon-export','glyphicon  glyphicon-eye-close','glyphicon  glyphicon-eye-open','glyphicon  glyphicon-facetime-video','glyphicon  glyphicon-fast-backward','glyphicon  glyphicon-fast-forward','glyphicon  glyphicon-file','glyphicon  glyphicon-film','glyphicon  glyphicon-filter','glyphicon  glyphicon-fire','glyphicon  glyphicon-flag','glyphicon  glyphicon-flash','glyphicon  glyphicon-floppy-disk','glyphicon  glyphicon-floppy-open','glyphicon  glyphicon-floppy-remove','glyphicon  glyphicon-floppy-save','glyphicon  glyphicon-floppy-saved','glyphicon  glyphicon-folder-close','glyphicon  glyphicon-folder-open','glyphicon  glyphicon-font','glyphicon  glyphicon-forward','glyphicon  glyphicon-fullscreen','glyphicon  glyphicon-gbp','glyphicon  glyphicon-gift','glyphicon  glyphicon-glass','glyphicon  glyphicon-globe','glyphicon  glyphicon-hand-down','glyphicon  glyphicon-hand-left','glyphicon  glyphicon-hand-right','glyphicon  glyphicon-hand-up','glyphicon  glyphicon-hd-video','glyphicon  glyphicon-hdd','glyphicon  glyphicon-header','glyphicon  glyphicon-headphones','glyphicon  glyphicon-heart','glyphicon  glyphicon-heart-empty','glyphicon  glyphicon-home','glyphicon  glyphicon-import','glyphicon  glyphicon-inbox','glyphicon  glyphicon-indent-left','glyphicon  glyphicon-indent-right','glyphicon  glyphicon-info-sign','glyphicon  glyphicon-italic','glyphicon  glyphicon-leaf','glyphicon  glyphicon-link','glyphicon  glyphicon-list','glyphicon  glyphicon-list-alt','glyphicon  glyphicon-lock','glyphicon  glyphicon-log-in','glyphicon  glyphicon-log-out','glyphicon  glyphicon-magnet','glyphicon  glyphicon-map-marker','glyphicon  glyphicon-minus','glyphicon  glyphicon-minus-sign','glyphicon  glyphicon-move','glyphicon  glyphicon-music','glyphicon  glyphicon-new-window','glyphicon  glyphicon-off','glyphicon  glyphicon-ok','glyphicon  glyphicon-ok-circle','glyphicon  glyphicon-ok-sign','glyphicon  glyphicon-open','glyphicon  glyphicon-paperclip','glyphicon  glyphicon-pause','glyphicon  glyphicon-pencil','glyphicon  glyphicon-phone','glyphicon  glyphicon-phone-alt','glyphicon  glyphicon-picture','glyphicon  glyphicon-plane','glyphicon  glyphicon-play','glyphicon  glyphicon-play-circle','glyphicon  glyphicon-plus','glyphicon  glyphicon-plus-sign','glyphicon  glyphicon-print','glyphicon  glyphicon-pushpin','glyphicon  glyphicon-qrcode','glyphicon  glyphicon-question-sign','glyphicon  glyphicon-random','glyphicon  glyphicon-record','glyphicon  glyphicon-refresh','glyphicon  glyphicon-registration-mark','glyphicon  glyphicon-remove','glyphicon  glyphicon-remove-circle','glyphicon  glyphicon-remove-sign','glyphicon  glyphicon-repeat','glyphicon  glyphicon-resize-full','glyphicon  glyphicon-resize-horizontal','glyphicon  glyphicon-resize-small','glyphicon  glyphicon-resize-vertical','glyphicon  glyphicon-retweet','glyphicon  glyphicon-road','glyphicon  glyphicon-save','glyphicon  glyphicon-saved','glyphicon  glyphicon-screenshot','glyphicon  glyphicon-sd-video','glyphicon  glyphicon-search','glyphicon  glyphicon-send','glyphicon  glyphicon-share','glyphicon  glyphicon-share-alt','glyphicon  glyphicon-shopping-cart','glyphicon  glyphicon-signal','glyphicon  glyphicon-sort','glyphicon  glyphicon-sort-by-alphabet','glyphicon  glyphicon-sort-by-alphabet-alt','glyphicon  glyphicon-sort-by-attributes','glyphicon  glyphicon-sort-by-attributes-alt','glyphicon  glyphicon-sort-by-order','glyphicon  glyphicon-sort-by-order-alt','glyphicon  glyphicon-sound-5-1','glyphicon  glyphicon-sound-6-1','glyphicon  glyphicon-sound-7-1','glyphicon  glyphicon-sound-dolby','glyphicon  glyphicon-sound-stereo','glyphicon  glyphicon-star','glyphicon  glyphicon-star-empty','glyphicon  glyphicon-stats','glyphicon  glyphicon-step-backward','glyphicon  glyphicon-step-forward','glyphicon  glyphicon-stop','glyphicon  glyphicon-subtitles','glyphicon  glyphicon-tag','glyphicon  glyphicon-tags','glyphicon  glyphicon-tasks','glyphicon  glyphicon-text-height','glyphicon  glyphicon-text-width','glyphicon  glyphicon-th','glyphicon  glyphicon-th-large','glyphicon  glyphicon-th-list','glyphicon  glyphicon-thumbs-down','glyphicon  glyphicon-thumbs-up','glyphicon  glyphicon-time','glyphicon  glyphicon-tint','glyphicon  glyphicon-tower','glyphicon  glyphicon-transfer','glyphicon  glyphicon-trash','glyphicon  glyphicon-tree-conifer','glyphicon  glyphicon-tree-deciduous','glyphicon  glyphicon-unchecked','glyphicon  glyphicon-upload','glyphicon  glyphicon-usd','glyphicon  glyphicon-user','glyphicon  glyphicon-volume-down','glyphicon  glyphicon-volume-off','glyphicon  glyphicon-volume-up','glyphicon  glyphicon-warning-sign','glyphicon  glyphicon-wrench','glyphicon  glyphicon-zoom-in','glyphicon  glyphicon-zoom-out'];
				$.get(contextPath + '/pages/auth/imgsDialog.jsp').done(function(result){
					var imgsDialog = $(result);
					var contents = new Array();
	                $.each(icons, function(){
	                    contents.push('<span class="menu-icon '+this+'" src="'+this+'"></span>');
	                });
					imgsDialog.find('#images').html(contents.join(''))
					.find('span')
					.on('click', function(){
						 form.find("#menuIcon").removeClass().addClass('menu-icon')
	                         .addClass($(this).attr('src')).attr('src',$(this).attr('src'));
						 imgsDialog.modal('hide');
					});
					imgsDialog.modal({
						keyboard: false,
	                    backdrop: false
					}).on({
						'hidden.bs.modal': function(){
							$(this).remove();
						}
					});
				});
			});
		};

		function validate(form, dialog, item, opreate){
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
	            		
	            		if(item != null){//problem
	            			console.log("aaa");
	            			url = baseUrl + 'addChildToParent.koala';	
	            			data += ("&parentId=" +item.id);
	        			}else{
	        				var url = baseUrl + 'add.koala';
	        			}
	        			if (item&&opreate =='modify') {
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
		
		deleteMenu = function(urls, grid) {
			var id = urls[0].id;
			var url = baseUrl + 'terminate.koala';
			$.post(url,{"menuResourceIds":id}).done(function(data){
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
				title : "菜单描述",
				name : "description",
				width : 150
			}];
		
	
		var url = contextPath + "/auth/menu/findAllMenusTree.koala";

		
		var buttons =[{
					content: '<ks:hasSecurityResource identifier="menuResourceManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
					action: 'add'
				},{
					content: '<ks:hasSecurityResource identifier="menuResourceManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
					action: 'modify'
				},{
					content: '<ks:hasSecurityResource identifier="menuResourceManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button></ks:hasSecurityResource>',
					action: 'delete'
				},{
					content: '<ks:hasSecurityResource identifier="menuResourceManagerGrantPermission"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>授权权限</button></ks:hasSecurityResource>',
					action: 'permissionAssign'
				}];
		
		
		

		
		$("<div/>").appendTo($("#tabContent>div:last-child")).grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: true,
             url: url,
             tree: {
             	column: 'name'
             }
        }).on({
        	'add': function(event, data){//data change item
        		var indexs = data.data;
                var grid = $(this);        	  
	            if(indexs.length > 1){
	                     grid.message({
	                    type: 'warning',
	                    content: '只能选择一条记录'
	                });
	                return;
	            }	          
				$.get(contextPath + '/pages/auth/menu-template.jsp').done(function(dialog) {
					initEditDialog(dialog, (data.data?data.item[0]:null), grid, "add");
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
	            $.get(contextPath + '/pages/auth/menu-template.jsp').done(function(dialog) {
					initEditDialog(dialog, data.item[0], grid, "modify");
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
	                	deleteMenu(data.item, grid);
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
				
				var menu = items[0];
				openTab('/pages/auth/permission-list.jsp', menu.name+'的权限管理', 'roleManager_' + menu.id, menu.id, {menuId : menu.id});
        	},
        	"assignPermission" : function(event, data){
				var grid = $(this);
				/*
				获取选中项的信息
				*/
        		$.get(contextPath + '/pages/auth/select-menu.jsp').done(function(data){
        			var dialog = $(data);
        			dialog.find('#save').click(function(){
        				var saveBtn = $(this);
        				var items = dialog.find('.selectMenuGrid').data('koala.grid').selectedRows();
        				
        				if(items.length == 0){
        					dialog.find('.selectMenuGrid').message({
        						type: 'warning',
        						content: '请选择要分配的menu'
        					});
        					return;
        				}
        				
        				saveBtn.attr('disabled', 'disabled');
        				
        				
        				
        				var data = "roleId="+roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&id=" + items[i].id;
        				}
        				
        			
        				$.get(contextPath + '/auth/role/grantMenuResources.koala', data).done(function(data){
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
       							title : "菜单名称",
       							name : "name",
       							width : 150
       						},
       					   {
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
								querys: [{title: '菜单名称', value: 'menuNameForSearch'}],
								url: contextPath + '/auth/role/findMenuResourceTreeSelectItemByRoleId.koala?roleId='+roleId
								/*tree: {
									column: 'name',
									
								}*/
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
			'removePermission' : function(event, data) {
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
						var url = contextPath + '/auth/user/terminateMenusByUser.koala';
						var params = "roleId="+roleId;
						for (var i = 0, j = data.item.length; i < j; i++) {
							params += ("&id=" + data.item[i].id);
						}
						
						$.get(url, params).done(function(data){
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
	});
</script>