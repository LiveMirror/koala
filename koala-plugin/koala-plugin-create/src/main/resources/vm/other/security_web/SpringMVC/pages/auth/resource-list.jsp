<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="resourceGrid"></div>
<script type="text/javascript" src="<c:url value='/js/security/resource.js' />"></script>
<script>
	$(function() {
		var columns = [{
			title : "资源名称",
			name : "name",
			width : 150
		}, {
			title : "资源标识",
			name : "identifier",
			width : 150
		}, {
			title : "资源描述",
			name : "desc",
			width : 250
		}, {
			title : "资源类型",
			name : "typeName",
			width : 150
		}];
		var buttons = [{
			content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>',
			action : 'add'
		}, {
			content : '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>',
			action : 'modify'
		}, {
			content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
			action : 'delete'
		}];
		$('#resourceGrid').grid({
			identity : 'id',
			columns : columns,
			buttons : buttons,
			isShowPages : false,
			url : contextPath + '/auth/Resource/findAllReourceTree.koala',
			tree : {
				column : 'name'
			}
		}).on({
			'add' : function(evnet, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录'
					})
					return;
				}
				if (indexs.length == 1) {
					resourceManager().add($(this), data.item[0]);
				} else {
					resourceManager().add($(this));
				}
			},
			'modify' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行修改'
					})
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行修改'
					})
					return;
				}
				resourceManager().modify(data.item[0], $(this));
			},
			'delete' : function(event, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要删除的记录'
					})
					return;
				}
				/*
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行删除'
					})
					return;
				}
				*/
 				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						resourceManager().deleteItem(data.item, $this);
					}
				});
			}
		});
	})
</script>