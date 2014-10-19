$(function() {
	$.get(contextPath + '/auth/currentUser/getUserDetail.koala').done(function(result) {
		var data = result.data;
		var userInfo = $('.user-info');
		userInfo.find('[data-id="name"]').text(data.name);
		userInfo.find('[data-id="userAccount"]').text(data.userAccount);
		userInfo.find('[data-id="createDate"]').text(data.createDate);
        userInfo.find('[data-id="lastModifyTime"]').text(data.lastModifyTime == null ? "" : data.lastModifyTime);
        userInfo.find('[data-id="email"]').text(data.email==null ? "您还没有邮箱，请添加邮箱！" : data.email);
		userInfo.find('[data-id="description"]').text(data.description==null ? "" : data.description);
		userInfo.find('[data-id="telePhone"]').text(data.telePhone==null ? "您还没有联系电话，请添加电话！" : data.telePhone);
		userInfo.find('[data-id="disabled"]').text(data.disabled ? "不可用":"可用");
	});
});
