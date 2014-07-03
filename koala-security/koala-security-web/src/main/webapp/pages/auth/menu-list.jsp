<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="menuListGrid"></div>
<script type="text/javascript" src="<c:url value='/js/security/menu.js' />"></script>
<script>
	$(function(){
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
			url = contextPath + "/auth/role/";
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
					content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-th-large"><span>分配url</button>',
					action : 'assignMenu'
				}, {
					content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
					action : 'removeMenuFromRole'
				}];
			}
		})();
		
		$('#menuListGrid').grid({
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
        	'moveUp': function(){
        		menuManager().moveUp($(this));
        	},
        	'moveDown': function(){
				menuManager().moveDown($(this));
        	}
        });
	});
</script>