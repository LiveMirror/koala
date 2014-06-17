<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="k" uri="http://www.openkoala.com/token"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="java.util.Date"%>
<%Long time = new Date().getTime();%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Koala权限系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/main.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/css/security.css' />"   rel="stylesheet">
    <link href="<c:url value='/css/koala.css' />?time=<%=time%>" rel="stylesheet">
    <link href="<c:url value='/lib/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />
    " rel="stylesheet">
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
	<input type="hidden" id="roleId" value="${roleId}" />
	<div class="g-head">
	    <nav class="navbar navbar-default">
	        <a class="navbar-brand" href="#"><img src="<c:url value='/images/global.logo.png'/>"/>Koala权限系统</a>
	        <div class="collapse navbar-collapse navbar-ex1-collapse">
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
	            <div class="btn-group navbar-right">
	                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
	                    <i class="glyphicon glyphicon-role"></i>
	                    <span class="isMasterRole">&nbsp;</span>
	                    <span class="caret"></span>
	                </button>
	                <ul class="dropdown-menu" id="allRolesId"></ul>
	            </div>
	        </div>
	    </nav>
	</div>
	<div class="g-body">
	    <div class="col-xs-2 g-sidec">
	        <ul class="nav nav-stacked first-level-menu">
	       		
	        </ul>
	    </div>
	    <div class="col-xs-10 g-mainc">
	        <ul class="nav nav-tabs" id="navTabs">
	            <li class="active"><a href="#home" data-toggle="tab">主页</a></li>
	        </ul>
	        <div class="tab-content" id="tabContent">
	            <div id="home" class="tab-pane active"></div>
	        </div>
	    </div>
	</div>
	<div id="footer" class="g-foot">
	    <span>Copyright © 2011-2013 Koala</span>
	</div>
	<script type="text/javascript" src="<c:url value='/lib/jquery-1.8.3.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/respond.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap/js/bootstrap.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/koala-ui.plugin.js' />?time=<%=time%>" ></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js' />"></script>
	<script type="text/javascript" src="<c:url value='/lib/koala-tree.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/lib/validate.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/main.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/js/security/role.js' />?time=<%=time%>" ></script>
    <script type="text/javascript" src="<c:url value='/js/security/user.js' />?time=<%=time%>" ></script>
	<script>
	$(function(){
		$.getJSON(contextPath + '/auth/role/findRoleDtosByUsername.koala', function(data) {
			console.info(data);
			$.each(data.result, function() {
				if(this.master){
					$('.isMasterRole').html(this.roleName);
				}else{
					$('#allRolesId').append('<li class="switchRole"><a href="#">'+this.roleName+'</li>');
				}
			  });
		}); 
		 $.get(contextPath + '/auth/menu/findMenuByUser.koala').done(function(data){
             $.each(data.data, function(){
                 var $li = $('<li class="folder"><a data-toggle="collapse" href="#menuMark'+this.id+'"><span class="'+this.icon+'"></span>&nbsp;'+this.name+'&nbsp;'+
                     '<i class="glyphicon glyphicon-chevron-left" style=" float: right;font-size: 12px;position: relative;right: 8px;top: 3px;"></i></a><ul id="menuMark'+this.id+'" class="second-level-menu in"></ul></li>');
                 $('.first-level-menu').append($li);
                 renderSubMenu(this.children, $li);
             });
             /*
             * 菜单收缩样式变化
             */
             var firstLevelMenu = $('.first-level-menu');
             firstLevelMenu.find('[data-toggle="collapse"]').each(function(){
                 var $this = $(this);
                 firstLevelMenu.find($(this).attr('href')).on({
                     'shown.bs.collapse': function(e){
                         $this.find('i:last').addClass('glyphicon-chevron-left').removeClass('glyphicon-chevron-right');
                     },
                     'hidden.bs.collapse': function(e){
                         $this.find('i:last').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-right');
                     }
                 });
             });
         });
	}); 
	var renderSubMenu = function(data, $menu){
					$.each(data, function(){
						if(this.menuType == "2"){
	                        var $li = $('<li class="folder"><a data-toggle="collapse" href="#menuMark'+this.id+'"><span class="'+this.icon+'"></span>&nbsp;'+this.name+'&nbsp;'+
	                            '<i class="glyphicon glyphicon-chevron-right pull-right" style="position: relative; right: 12px;font-size: 12px;"></i></a><ul id="menuMark'+this.id+'" class="second-level-menu collapse"></ul></li>');
	                        $li.appendTo($menu.find('.second-level-menu:first')).find('a').css('padding-left', parseInt(this.level)*18+'px');
	                        renderSubMenu(this.children, $li);
	                    }else{
	                        var $li = $(' <li class="submenu" data-role="openTab" data-target="'+this.identifier+'" data-title="'+this.name+'" ' +
	                            'data-mark="menuMark'+this.id+'"><a ><span class="'+this.icon+'"></span>&nbsp;'+this.name+'</a></li>');
	                        $li.appendTo($menu.find('.second-level-menu:first')).find('a').css('padding-left', parseInt(this.level)*18+'px');
	                    }
					});
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
			}
	
		$('.switchRole').click(function(){
			console.info("click...");
		});
		
	</script>
</body>
</html>