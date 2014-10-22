$(function(){
	/*
	 重置菜单栏容器默认高度
	 */
	var self = $(this);
	var $window = $(window);
	$window.on('resize', function(){
		var windowWidth = $window.width();
		var sidebar = $('.g-sidec');
		if(windowWidth < 768){
			$('#sidebar-collapse').hide();
			sidebar.css('height', 'auto');
			return;
		}else{
            changeHeight();
        }
	});
	/**
	 * 根据内容改变高度
	 */
	var changeHeight = function(){
		var sidebar = $('.g-sidec');
		var sidebarHeight = sidebar.outerHeight();
		var headerHeight = $('.g-head').outerHeight();
		var windowHeight = $window.height();
		var bodyHeight = $(document).height();
		if(bodyHeight >  windowHeight){
			windowHeight =  bodyHeight;
		}
		var footHeight = $('#footer').outerHeight();
		var height =  windowHeight - headerHeight - footHeight;
		sidebarHeight < height && sidebar.css('height', height);
	};
	/*
	 加载DIV内容
	 */
	var loadContent = function(obj, target){
		$.get(target).done(function(data){
				obj.html(data);
			}).fail(function(){
				throw new Error('加载失败');
			}).always(function(){
				changeHeight();
			});
	};
	loadContent($('#home'), 'pages/welcome.html');	
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
        })
    });
	/*
	 *菜单点击事件
	 */
	var $submenu = $('.first-level-menu').find('li');
	$submenu.on('click', function(){
		var $this = $(this);
		if($this.hasClass('active')) {
			return;
		}
		clearMenuEffect();
		$this.addClass('active').parent().closest('li').addClass('active').parent().closest('li').addClass('active');
		var target = $this.data('target');
		var title = $this.data('title');
		var mark = $this.data('mark');
		if(target && title && mark ){
			$this.openTab(target, title, mark);
		}
	})
	/*
	 * 清除菜单效果
	 */
	var clearMenuEffect = function(){
		$submenu.each(function(){
			var $menuLi = $(this);
			$menuLi.hasClass('active') && $menuLi.removeClass('active').parent().parent().removeClass('active');
		});
	};
	/*
	 点击主页tab事件
	 */
	$('a[href="#home"]').on('click', function(){
		clearMenuEffect();
		$('.g-sidec').find('li[data-mark="home"]').click()
	});
	/*
	 *打开一个Tab
	 */
	$.fn.openTab = function(target, title, mark, id, param){
		var mainc =   $('.g-mainc');
		var tabs = mainc.find('#navTabs');
		var contents =  mainc.find('#tabContent');
		var content = contents.find('#'+mark);
		if(content.length > 0){
			content.attr('data-value', id);
			loadContent(content, target);
			tabs.find('a[href="#'+mark+'"]').tab('show');
			tabs.find('a[href="#'+mark+'"]').find('span').text(title);
			return;
		}
		var tab = $('<li><a href="#'+mark+'" data-toggle="tab"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button><span>'+title+'<span></a></li>');
		content = $('<div id="'+mark+'" class="tab-pane" data-value="'+id+'"></div>');
		content.data(param);
		loadContent(content, target);
		contents.append(content);
		var closeBtn = tab.appendTo(tabs).on('click',function(){
			var $this = $(this);
			if($this.hasClass('active')){
				return;
			}
			var $li = $('.g-sidec').find('li[data-mark="'+mark+'"]');
			$li.click();
			if($li.parent().hasClass('collapse')){
				$li.parent().prev('a').click();
			}
		}).find('a:first')
			.tab('show')
			.find('.close');
		closeBtn.css({position: 'absolute', right: (closeBtn.width()-10) + 'px', top: -1 + 'px'})
			.on('click',function(){
				var prev =  tab.prev('li').find('a:first');
				content.remove() && tab.remove();
				var herf = prev.tab('show').attr('href').replace("#", '');
				var $menuLi =  $('.g-sidec').find('li[data-mark="'+herf+'"]');
				if($menuLi.length){
					$menuLi.click();
				}else{
					clearMenuEffect();
				}
			});
	};
	/*
	 修改密码
	 */
	self.on('modifyPwd',function(){
		$('body').modifyPassword({
			service: 'auth/User/updatePassword.koala'
		});
	});
	/*
	 切换用户
	*/
	self.on('switchUser',function(){
		window.location.href = "j_spring_security_logout";
	});
	/*
	注销
	*/
	self.on('loginOut',function(){
		window.location.href = "j_spring_security_logout";
	});
	$('#userManager').find('li').on('click', function(){
		self.trigger($(this).data('target'));
	});
});
