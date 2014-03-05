<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="resourceTypeGrid"></div>
<script type="text/javascript" src="<c:url value='/js/security/resourceType.js' />"></script>
<script>
	$(function() {
		var columns = [{
			title : "资源类型名称",
			name : "name",
			width : 250
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
		$('#resourceTypeGrid').grid({
			identity : 'id',
			columns : columns,
			buttons : buttons,
			url : contextPath + '/auth/ResourceType/pageJson.koala'
		}).on({
			'add' : function() {
				resourceTypeManager().add($(this));
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
				resourceTypeManager().modify(data.item[0], $(this));
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
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						resourceTypeManager().deleteItem(data.item, $this);
					}
				});
			}
		});
	})
</script>