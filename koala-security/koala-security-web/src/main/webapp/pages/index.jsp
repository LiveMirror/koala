<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="koala" uri="http://www.openkoala.org/security" %>
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
    	.nav-stacked{
    	 position:relative;
    	 top:0;
    	 bottom:0;
    	 left:0;
    	 right:0;
    	}
    	.folder > a{
         background:#ccc;
    	}
    	.leaf_node > a .glyphicon{
    	  display:none;
    	}
    	.menu_name{
    	  font-size:16px;
    	}
    	.leaf_node{
    	 text-align:center;
    	}
    	.leaf_node span{
    	  font-size:14px !important;
    	}
    	.user_name{
    	  color:#fff;
    	}
    	.yhmc{
    	padding-top:10px;
    	float:left;
    	}
    	.m_left{
    	margin-left:20px;
    	}
    	/*
    	*阶梯效果
    	*/
    	.leaf_node:hover{
    	   -webkit-transform: scale(1.1);
		  -moz-transform: scale(1.1);
		  -ms-transform: scale(1.1);
		  -o-transform: scale(1.1);
		  transform: scale(1.1);
		  box-shadow: 0 0 10px rgba(0, 0, 0, 0.75);
		  z-index: 3;
		  
    	}
    	.nav-stacked a.active-1 {
		  -webkit-transform: scale(1.1) translateX(24px);
		  -moz-transform: scale(1.1) translateX(24px);
		  -ms-transform: scale(1.1) translateX(24px);
		  -o-transform: scale(1.1) translateX(24px);
		  transform: scale(1.1) translateX(24px);
		  box-shadow: 0 0 10px rgba(0, 0, 0, 0.75);
		  z-index: 3;
		}
		.nav-stacked a.active-2 {
		  -webkit-transform: scale(1.07) translateX(12px);
		  -moz-transform: scale(1.07) translateX(12px);
		  -ms-transform: scale(1.07) translateX(12px);
		  -o-transform: scale(1.07) translateX(12px);
		  transform: scale(1.07) translateX(12px);
		  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
		  z-index: 2;
		}
		.nav-stacked a.active-3 {
		  -webkit-transform: scale(1.04) translateX(4px);
		  -moz-transform: scale(1.04) translateX(4px);
		  -ms-transform: scale(1.04) translateX(4px);
		  -o-transform: scale(1.04) translateX(4px);
		  transform: scale(1.04) translateX(4px);
		  z-index: 1;
		}


#btn1{

     
        cursor: pointer;
        height: 45px;
        width: 45px;
        position: relative;
        float:right;
        top: -3px;
        left:5px;
}


label{

     padding-right:3px;
}


div.btn-group > span {
        
        font-size: 15px;
        line-height: 39px;
        padding:0 2px 0px 3px;
        
        
        background:#fff;
     
      
       
}      



	
	
    </style>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
        console.log(contextPath);
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
	                <label for = "userAccount" class = "user_name yhmc">用户  : </label>
	               
                   	<span><koala:user property="name"/></span>
                   	
                   	
                   
	                <img class=" dropdown-toggle" data-toggle="dropdown" id='btn1'  src="../images/setMenu.png"  >
	                
	                
	             
	                
	              <!--  <i class = "menu-icon glyphicon  glyphicon-cog"></i>--> 
	                <ul class="dropdown-menu" id="userManager" >
	                    <li data-target="loginOut"><a href="#">注销</a></li>
	                    <li data-toggle="modal" data-target="#userDetial"><a href="#">用户详细</a></li>
	                    <li data-toggle='modal' data-target="#rolesToggle"><a href="#">角色切换</a></li>
	                    
	                    
	                </ul>
	                
	             
	                
	            </div>
	            <div class="btn-group navbar-right">
	                <label for = "roles" class = "user_name">角色 :</label>
	            	<span id="roles">
	            		<koala:user property="roleName"/>
	                </span>
	                
	                &nbsp;
	                
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
	
  <!-- 切换角色模态框 -->
	
<div class="modal fade" id="rolesToggle" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
     <div class="modal-content">
     
       <div class="modal-header">
         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
         <h4 class="modal-title" id="myModalLabel">切换角色</h4>
      </div>
      <div class="modal-body">
      
    <div class='grid-body' style='width:100%;min-width:300px;border:0.5px #d0d0d0 groove;'>
      
     <div class='grid-table-head'>
      
      <table class='table table-bordered'>
    
       <thead>
         
         <tr>
            <th style='width:100px;'>角色名称</th>
            <th style='width:auto'>描述</th>
        </tr>
      
         
      
       </thead>
      </div>
      <div class='grid-table-body'></div>
      <tbody>
         <tr>
            <td>superAdmin</td>
            <td></td>
         </tr>
         
         <tr>
            <td>test</td>
            <td></td>
         </tr>
         
          
         <tr>
            <td>test</td>
            <td></td>
         </tr>
         
          
         <tr>
            <td>test</td>
            <td></td>
         </tr>
         </tbody>
     </table>
     
     
    
   </div>
      
      
       
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
        <button type="button" class="btn btn-success">取消</button>
      </div>
    </div>
 </div>
  
	<script type="text/javascript" src="../lib/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="../lib/respond.min.js"></script>
	<script type="text/javascript" src="../lib/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../lib/koala-tree.js"></script>
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
			var roles = $("#roles");
			roles.change();
		});
		
		/*根据roleid获取菜单*/
		$("#roles").change(function(){
			var roleName = '<koala:user property="roleName"/>';
			console.log(roleName);
				
				url = contextPath + "/auth/menu/findAllMenusByUserAsRole.koala?"+new Date().getTime();
			$.get(
				url,
				{'roleName':roleName},
				function(menuData){
					var menu = initMenu(menuData.data);
					
					$("#roleMenu").empty().append(menu);
					
					/*删除上一个角色打开的tab*/
					$("#navTabs").children().each(function(i, t){
						(i > 0) ? $(t).remove() : "";
					});
					$("#tabContent").children().each(function(i, t){
						(i > 0) ? $(t).remove() : "";
					});
					
					/*激活第一个tab*/
					$("#navTabs").find("a").click();
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
		/*$menu.find('[data-toggle="collapse"]').each(function(){
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
		});*/
		
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
							<a href="#menuMark'+ d.id +'" class = "asd'+d.id+'" onclick="click_here('+d.id+')"> \
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
	<script type="text/javascript">
	    function click_here(data){
	    	$(".asd"+data).next('.nav-stacked').toggle(600);
	      }
	    $(function(){
	    	$("#userManager").find('li').on("click",function(){
	    		var indexs = $(this).index();
	    		//console.log("aasaa"+indexs);
	    		if(indexs == 2){
	    			$.get(contextPath + '/pages/auth/userDetial.jsp').done(function() {
	    				userDetial();
	    			});
	    			function userDetial(){
	    				console.log("2222");
	    			}
	    			}
	    	});
	    });
	</script>
</body>
</html>
>>>>>>> origin/master
