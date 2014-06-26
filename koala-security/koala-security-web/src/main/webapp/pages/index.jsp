<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala权限系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="../lib/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../css/main.css"/>
    <link rel="stylesheet" href="../css/security.css"/>
    <link rel="stylesheet" href="../css/koala.css"/>
    <link rel="stylesheet" href="../lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <style>
    	select{
    		padding:4px;
    		margin:2px;
    		font-size:16px;
    		min-height:20px;
    		min-width:100px;
    	}
    	
    	.folder > ul{
    		padding-left:20px;
    	}
    	.folder > a{
    		background:#ccc;
    	}
    	.leaf_node > a .glyphicon{
    		display:none;
    	}
    </style>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>

<body>
	<input type="hidden" id="roleId" value="${roleId}" />
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#">
	        	<img src="../images/global.logo.png"/>
	        	<span style="font-weight:800;">Koala权限系统</span>
	        </a>
	        
	        <div class="collapse navbar-collapse navbar-ex1-collapse">
	            <!-- 账号信息 -->
	            <div class="btn-group navbar-right">
	                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                    <i class="glyphicon glyphicon-user"></i>
	                    <span>&nbsp;<shiro:principal /></span>
	                    <span class="caret"></span>
	                </button>
	                <ul class="dropdown-menu" id="userManager">
	                    <li data-target="modifyPwd"><a href="#">修改密码</a></li>
	                    <li data-target="loginOut"><a href="#">注销</a></li>
	                </ul>
	            </div>
	            <!--  -->
	            <div class="btn-group navbar-right">
	            	<select id="roles"></select>
	                <ul class="dropdown-menu" id="allRolesId"></ul>
	            </div>
	        </div>
	    </nav>
	</div>
	
	<div class="g-body">
		<!-- 左边导航 -->
	    <div class="col-xs-2 g-sidec" id="roleMenu"></div>
	    <!-- 右边内容 -->
	    <div class="col-xs-10 g-mainc">
	        <ul class="nav nav-tabs" id="navTabs">
	            <li class="active"><a href="#home" data-toggle="tab">主页</a></li>
	        </ul>
	        <div class="tab-content" id="tabContent">
	            <div id="home" class="tab-pane active"></div>
	        </div>
	    </div>
	</div>
	<div id="footer" class="g-foot"><span>Copyright © 2011-2013 Koala</span></div>
	<script type="text/javascript" src="../lib/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="../lib/respond.min.js"></script>
	<script type="text/javascript" src="../lib/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../lib/koala-ui.plugin.js" ></script>
	<script type="text/javascript" src="../lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript" src="../lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="../lib/validate.js"></script>
	<script type="text/javascript" src="../js/security/menu.js"></script>
	<script type="text/javascript" src="../js/main.js" ></script>
    <script type="text/javascript" src="../js/security/role.js" ></script>
    <script type="text/javascript" src="../js/security/user.js" ></script>
	<script>
	$(function(){
		/*获取用户的角色*/
		$.getJSON(contextPath + '/auth/role/findRolesByUsername.koala', function(data) {
			var roles = $("#roles").empty();
			$.each(data.result, function(i,role) {
				roles.append($("<option/>").attr("value",role.roleId).html(role.roleName));
			});
			roles.change();
		});
		
		/*根据roleid获取菜单*/
		$("#roles").change(function(){
			var roleId = $(this).val(),
				roleName = $(this).find("option:selected").text(),
				url = contextPath + "/auth/menu/findAllMenusByUserAsRole.koala";
			$.get(
				url,
				{"roleId":roleId,"roleName":roleName},
				function(menuData){
					var menu = initMenu(menuData.data);
					$("#roleMenu").empty().append(menu);
					
					/*生成角色菜单*/
					menu.delegate(".leaf_node","click",function(){
						var thiz 	= $(this),
							url 	= thiz.attr("url"),
							title 	= thiz.find('.menu_name').html(),
							mark 	= thiz.attr('mark');
						
						if(title && url){
							mark = openTab(url, title, mark);
							if(mark){
								thiz.attr("mark",mark);
							}
						}
					});
				},"json");
		});
	});
	
	var renderSubMenu = function(data, $menu){		
		$menu.find('[data-toggle="collapse"]').each(function(){
		    var $this = $(this);
		    $menu.find($(this).attr('href')).on({
		        'shown.bs.collapse': function(e){
		        	e.stopPropagation();
		        	e.preventDefault();
		            $this.find('i:last').addClass('glyphicon-chevron-left').removeClass('glyphicon-chevron-right');
		        },
		        'hidden.bs.collapse': function(e){
		        	e.stopPropagation();
		        	e.preventDefault();
		            $this.find('i:last').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-right');
		        }
		    });
		});
		
		$menu.find('li.submenu').on('click', function(){
			var $this = $(this);
				
			$('.first-level-menu').find('li').each(function(){
				var $menuLi = $(this);
				$menuLi.hasClass('active') && $menuLi.removeClass('active').parent().parent().removeClass('active');
			});
			
			$this.addClass('active').parents().filter('.folder').addClass('active');
			var target = $this.data('target');
			var title = $this.data('title');
			var mark = $this.data('mark');
				if(target && title && mark ){
					openTab(target, title, mark);
				}
			});
		};
	
		$('.switchRole').click(function(){
			console.info("click...");
		});
		
		/*判断一个对象是否数组*/
		function isArray(o){
			return '[object Array]' == Object.prototype.toString.call(o);
		}
		
		/*递归初始化菜单*/
		function initMenu(data){
			var menu = $('<ul class="nav nav-stacked"></ul>');
			var node;
			
			$.each(data,function(i,d){
				if(!d.name) return;
				
				
				node = $('<li class="node"> \
							<a href="#menuMark"'+ d.id +'"> \
								<span class="'+d.icon+'"></span> \
								<span class="menu_name">' + d.name + '</span> \
								<i class="glyphicon glyphicon-chevron-right pull-right" style="position:relative;right:12px;font-size:12px;"></i> \
							</a> \
						</li>');
				
				menu.append(node);
				
				/*如果children有值，该节点将不会是叶子节点*/
				if(isArray(d.children) && d.children.length > 0){
					node.addClass("folder").append(initMenu(d.children));
				} else {
					node.addClass("leaf_node").attr("url",d.url);
				}
			});
			return menu;
		}
	</script>
</body>
</html>