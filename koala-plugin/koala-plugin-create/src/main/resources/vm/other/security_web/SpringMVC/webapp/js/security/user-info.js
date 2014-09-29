$(function() {
	$.get(contextPath + '/auth/currentUser/getUserDetail.koala').done(function(result) {
		var data = result.data;
		var userInfo = $('.user-info');
		userInfo.find('[data-id="name"]').text(data.name);
		userInfo.find('[data-id="userAccount"]').text(data.userAccount);
		userInfo.find('[data-id="createDate"]').text(data.createDate);
		userInfo.find('[data-id="email"]').text(data.email);
		userInfo.find('[data-id="description"]').text(data.description);
		userInfo.find('[data-id="telePhone"]').text(data.telePhone);
		userInfo.find('[data-id="disabled"]').text(data.disabled?"不可用":"可用");
	});
});
