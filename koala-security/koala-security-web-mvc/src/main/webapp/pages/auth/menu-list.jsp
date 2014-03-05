<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="menuListGrid"></div>
<script type="text/javascript" src="<c:url value='/js/security/menu.js' />"></script>
<script>
	$(function(){
		var columns = [ 
					{
						title : "菜单名称",
						name : "name",
						width : 150
					},
					{
						title : "菜单标识",
						name : "identifier",
						width : 450
					},
					{
						title : "菜单类型",
						name : "menuType",
						width : 150,
						render: function(item, name, index){
							return item[name] == '2' ? '目录':'菜单';
						}
					},
					{
						title : "菜单图片",
						name : "icon",
						width : 150,
						render: function(item, name, index){
							return '<span class="'+item[name]+'"></span>'
						}
					},
					{
						title : "菜单描述",
						name : "desc",
						width : 150
					}
				];
		var buttons = [
			{content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>', action: 'add'},
			{content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>', action: 'modify'},
			{content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>', action: 'delete'},
			{content: '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-arrow-up"><span>上移</button>', action: 'moveUp'},
			{content: '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-arrow-down"><span>下移</button>', action: 'moveDown'}
		];
		$('#menuListGrid').grid({
			 identity: 'id',
             columns: columns,
             buttons: buttons,
             isShowPages: false,
             url: contextPath + '/auth/Menu/findAllMenuTreeRows.koala',
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
	                })
	                return;
	            }
	            if(indexs.length == 1){
	            	var data = data.item[0];
	            	if(data.menuType == '1'){
	            		 $this.message({
	 	                    type: 'warning',
	 	                    content: '菜单下不能新增菜单！'
	 	                })
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
	                })
	                return;
	            }
	            if(indexs.length > 1){
	                $this.message({
	                    type: 'warning',
	                    content: '只能选择一条记录进行修改'
	                })
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
		            })
		             return;
	            }
	            if(indexs.length > 1){
	                $this.message({
	                    type: 'warning',
	                    content: '只能选择一条记录进行删除'
	                })
	                return;
	            }
	            $this.confirm({
	                content: '确定要删除所选记录吗?',
	                callBack: function(){menuManager().deleteItem(data.item[0], $this);}
	            });
        	},
        	'moveUp': function(){
        		menuManager().moveUp($(this));
        	},
        	'moveDown': function(){
				menuManager().moveDown($(this));
        	}
        });
	})
</script>