$(function() {
	var employeeId = $('.employee-detail').parent().attr('data-value');
	$.get(contextPath + '/employee/get/' + employeeId + '.koala').done(function(result) {
		var data = result.data;
		var employeeDetail = $('.employee-detail');
		$('#navTabs').find('[href="#employeeDetail"] span').text(data.name);
		employeeDetail.find('[data-id="sn"]').text(data.sn);
		employeeDetail.find('[data-id="name"]').text(data.name);
		employeeDetail.find('[data-id="gender"]').text(data.gender == 'MALE' ? '男' : '女');
		employeeDetail.find('[data-id="idNumber"]').text(data.idNumber == null ? '':data.idNumber);
		employeeDetail.find('[data-id="organizationName"]').text(data.organizationName == null ? '':data.organizationName);
		employeeDetail.find('[data-id="postName"]').text(data.postName == null ? '':data.postName);
		employeeDetail.find('[data-id="mobilePhone"]').text(data.mobilePhone == null ? '':data.mobilePhone);
		employeeDetail.find('[data-id="familyPhone"]').text(data.familyPhone == null ? '':data.familyPhone);
		employeeDetail.find('[data-id="email"]').text(data.email == null ? '':data.email);
		employeeDetail.find('[data-id="entryDate"]').text(data.entryDate);
		employeeDetail.find('[data-id="additionalPostNames"]').text(data.additionalPostNames == null ? '':data.additionalPostNames);
		employeeDetail.find('[data-id="changeDepartment"],[data-id="changeJob"]').off('click');
		employeeDetail.find('[data-id="returnBtn"]').on('click', function() {
			$('.g-mainc').find('[href="#employeeDetail"]').find('button').click();
		});
		employeeDetail.find('[data-id="changePostBtn"]').on('click', function() {
			changePost().init(employeeId);
		});
	});
});
