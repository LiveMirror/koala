$(function(){
	var content = $('#content');
	var submenu = $('.u-menu').find('li');
	submenu.on('click', function(){
		var $this = $(this);
		if(!$this.hasClass('active')){
			submenu.removeClass('active');
			$this.addClass('active');
		}
		var href = $this.data('href');
		var title = $this.data('title');
		if(title && href){
			loadContent(title, href);
		}
	});
	
	var loadContent = function(title, href){
		$('#contentTitle').html(title);
		$.get(href).done(function(data){
			content.html(data);
		});
	};
	
	loadContent('项目列表', 'pages/cis/project-list.html');
	
	$('#loginOutBtn').on('click', function(){
		window.location.href = 'login.html';
	});
	
	$('#modifyPwd').on('click', function(){
		$('body').modifyPassword({
			service: 'auth/User/updatePassword.koala'
		});
	});
	
	 $('body').keydown(function(e) {
	     if (e.keyCode == 13) {
	         e.preventDefault();
	         e.stopPropagation();
	     }
	 });
	
});

