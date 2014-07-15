<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--<script type="text/javascript" src="<c:url value='/js/security/menu.js' />"></script>-->

<!-- add here -->
<link rel="stylesheet" href="../lib/validateForm/css/style.css"/>
<script src="../lib/validateForm/validateForm.js"></script>
<!-- end add here -->

<script>
	$(function(){
		
		//add here
		var baseUrl = contextPath + '/auth/menu/';
		var dialog 		= null;	//对话框
		var parentName 	= null;	//父资源名称
		var name 		= null; //资源名称
		var identifier 	= null; //资源标识
		//var url     	= null;
		var desc 		= null; //资源描述
		var menuImg 	= null; //菜单图片
		var menuImgBtn 	= null; //菜单图片按钮
		//var dataGrid 	= null; //Grid对象
		var opreate 	= null;
		//var parentId 	= null;
		//var parentLevel = null;
		//end add here
		
		function initEditDialog(data, item, grid) {
			//console.log(data);
			
			dialog = $(data);
			
		    //console.log(item);
		    
			dialog.find('.modal-header').find('.modal-title').html( item ? '修改菜单信息' : '添加菜单');
			
			var form = dialog.find(".menu_form");
			 parentName = dialog.find('#parentName');
			 name = dialog.find('#name');
			 //url = dialog.find("#menuUrl");
			 identifier = dialog.find('#identifier');
			 menuImg = dialog.find('#menuIcon');
			 menuImgBtn = dialog.find('#iconBtn');
			 desc = dialog.find('#desc');	
			
			
			
			validate(form, dialog, item);
			
			if(item){
				form.find("input[name='name']").val(item.name);
				form.find("input[name='identifier']").val(item.identifier);
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
			
			menuImg.addClass('glyphicon glyphicon-list-alt').attr('src', 'glyphicon glyphicon-list-alt');
			
			if(opreate == 'modify'){
				setData(item);
			}
			
			menuImgBtn.on('click', function(){
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
						 menuImg.removeClass().addClass('menu-icon')
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
			
			var setData = function(item){
				//console.log(JSON.stringify(item));
				
				parentName.closest('.form-group').hide();
				name.val(item.name);
				desc.val(item.desc);
				identifier.val(item.identifier);
				dialog.find("#menuUrl").val(item.url);
				menuImg.removeClass().addClass('menu-icon').addClass(item.icon).attr('src', item.icon);
			};
			
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
	        				//console.log(data);
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
		
		deleteMenu = function(urls, grid) {
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
				name : "description",
				width : 150
			}];
		
	
		var url = contextPath + "/auth/menu/findAllMenusTree.koala";

		
		var buttons = (function(){
			if(roleId){
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
		
		
		//add here
		
		
		
		
		
		
		
		
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
        	'add': function(evnet, item){//data change item
        		/*var indexs = data.data;
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
	            }*/
	            
        		var thiz = $(this);
				$.get(contextPath + '/pages/auth/menu-template.jsp').done(function(data) {
					initEditDialog(data, null, thiz);
				});
	            
        	},
        	'modify': function(event, data){
        		/*var indexs = data.data;
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
				menuManager().modify(data.item[0], $(this));*/
				
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
					initEditDialog(dialog, data.item[0], grid);
				});
        	},
        	'delete': function(event, data){
        		/*var indexs = data.data;
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
	            });*/
	            
	            
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
        	"assignMenu" : function(event, data){
				var grid = $(this);
				/*
				获取选中项的信息
				*/
				//console.log(grid);
        		$.get(contextPath + '/pages/auth/select-menu.jsp').done(function(data){
        			var dialog = $(data);
        			//console.log(dialog);
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
        				
        				/*var data = {};
        				var mDTOs = [];
        				data.roleId = roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					var dto = {};
        					dto.id = items[i].id;
        					mDTOs.push(dto);
        				}
        				data.MenuResourceDTOs = mDTOs;*/
        				
        				
        				var data = "roleId="+roleId;
        				for(var i=0,j=items.length; i<j; i++){
        					data += "&id=" + items[i].id;
        				}
        				
        				/*$.ajax({
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
        			})*/
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
       							title : "菜单标识",
       							name : "identifier",
       							width : 150
       						},{
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
			'removeMenuFromRole' : function(event, data) {
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