var userManager = function() {
	var baseUrl = contextPath + '/auth/employeeUser/';
	var dialog = null;
	//对话框
	var userName = null;
	//用户名
	var userAccount = null;
	//用户账号
	var description = null;
	//描述
	var isEnable = null;
	//是否启用
	var dataGrid = null;
	//Grid对象


    /* 与组织集成需要的字段*/
    var orgEmployee = null;

    var employeeId = null;

    /*
     *新增
     */
	var add = function(grid) {
		dataGrid = grid;
        console.log(dataGrid);
		$.get(contextPath + '/pages/auth/user-template.jsp').done(function(data) {
			addInit(data);
		});
	};
	function addInit(data){
		dialog = $(data);
		userName = dialog.find('#userName');
		userAccount = dialog.find('#userAccount');
		description = dialog.find('#description');
        orgEmployee = dialog.find("#orgEmployee");
        orgEmployee.find('[data-toggle="dropdown"]').on('click', function(){
            selectEmployees();
        });
        employeeId = orgEmployee.find('input');
        dialog.find('#save').on('click', function(){
			save();
		}).end().modal({
			keyboard : false
		}).on({
			'hidden.bs.modal' : function() {
				$(this).remove();
			},
			'complete' : function() {
				dataGrid.message({
					type : 'success',
					content : '保存成功'
				});
				$(this).modal('hide');
                dataGrid.grid('refresh');
			}
		});
	}
	/*
	 * 修改
	 */
	var modify = function(item, grid) {
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/user-template.jsp').done(function(data) {
			init(data, item);
			setData(item);
		});
	};
	/*
	 * 重置密码
	 */
	var resetPassword = function(item, grid) {
		var dataGrid = grid;
		console.log(item.id);
		$.post(contextPath + '/auth/user/resetPassword.koala?userId=' + item.id).done(function(data) {
			if (data.success) {
				dataGrid.message({
					type : 'success',
					content : '重置密码成功，初始密码为888888！'
				});
			} else {
				dataGrid.message({
					type : 'error',
					content : data.errorMessage
				});
			}
		}).fail(function(data) {
			dataGrid.message({
				type : 'error',
				content : '重置密码失败'
			});
		});
	};
	/*
	 * 激活
	 */
	var available = function(item, grid) {
		var dataGrid = grid;
		$.post(contextPath + '/auth/user/activate.koala?userId=' + item.id).done(function(data) {
		    
			if (data.success == true) {				
				dataGrid.message({
					type : 'success',
					content : '用户已经激活！'
				});
				grid.grid('refresh');
			} else {
				dataGrid.message({
					type : 'error',
					content : data.errorMessage
				});
			}
		}).fail(function(data) {
			dataGrid.message({
				type : 'error',
				content : '激活失败'
			});
		});
	};
	/*
	 禁用
	 */
	var forbidden = function(item, grid) {
		var dataGrid = grid;
	    console.log(item.id);
		$.post(contextPath + '/auth/user/suspend.koala?userId=' + item.id).done(function(data) {
		    
			if (data.success == true) {				
				dataGrid.message({
					type : 'success',
					content : '用户禁用成功！'
				});
				grid.grid('refresh');
			} else {
				dataGrid.message({
					type : 'error',
					content : data.errorMessage
				});
			}
		}).fail(function(data) {
			dataGrid.message({
				type : 'error',
				content : '禁用失败'
			});
		});
	};

    /**
     * 删除多个用户
     * @param users 多个用户
     * @param grid
     */
	var deleteUser = function(users, grid) {

        var data = "";
        $.each(users, function(i, user){
            data += ("userIds=" + user.id + "&");
        });
        data = data.substring(0, data.length-1);

        dataGrid = grid;
		$.post(contextPath + '/auth/user/terminate.koala', data).done(function(data){
			if(data.success){
				dataGrid.message({
					type: 'success',
					content: '撤销成功'
				});
				dataGrid.grid('refresh');
			}else{
				$('body').message({
					type: 'error',
					content: data.errorMessage
				});
			}
		}).fail(function(data){
			dataGrid.message({
				type: 'error',
				content: '撤销失败'
			});
		});
	};

	/**
	 * 初始化
	 */
	var init = function(data, item) {
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html( item ? '修改用户信息' : '添加用户');
		userName = dialog.find('#userName');
		userAccount = dialog.find('#userAccount');
		description = dialog.find('#description');
		isEnable = dialog.find('[name="isEnable"]');
        isEnable = dialog.find('[name="isEnable"]');

        //与组织集成
        orgEmployee = dialog.find("#orgEmployee");
        if(item.employeeName != null){
            orgEmployee.find('[data-toggle="item"]').html(item.employeeName);
        }
        orgEmployee.find('[data-toggle="dropdown"]').on('click', function(){
            selectEmployees();
        });
        employeeId = orgEmployee.find('input');<!-- 与组织集成 end-->

        isEnable.on('click', function() {
			isEnable.each(function() {
				$(this).parent().removeClass('checked');
			});
			$(this).parent().addClass('checked');
		});
		dialog.find('#save').on('click', function() {
			save(item);
		}).end().modal({
			keyboard : false
		}).on({
			'hidden.bs.modal' : function() {
				$(this).remove();
			},
			'complete' : function() {
				dataGrid.message({
					type : 'success',
					content : '保存成功'
				});
				$(this).modal('hide');
				dataGrid.grid('refresh');
			}
		});
	};

	/*
	 *设置值
	 */
	var setData = function(item) {
		userName.val(item.name);
		userAccount.val(item.userAccount).attr('disabled', 'disabled');
		description.val(item.description);
		if (!item.valid) {
			dialog.find('[name="isEnable"][value="true"]').removeAttr('checked', 'checked').parent().removeClass('checked');
			dialog.find('[name="isEnable"][value="false"]').attr('checked', 'checked').parent().addClass('checked');
		}
	};
	/*
	 *   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(item) {
		if (!validate(item)) {
			dialog.find('#save').removeAttr('disabled');
			return false;
		}
		var url = baseUrl + 'add.koala';
		if (item) {
			url = baseUrl + 'update.koala';
		}
		
		$.post(url, getAllData(item)).done(function(data) {
			if (data.success) {
				dialog.trigger('complete');
			} else {
				dialog.find('.modal-content').message({
					type : 'error',
					content : data.errorMessage
				});
			}
			dialog.find('#save').removeAttr('disabled');
		});
	};

	/**
	 * 数据验证
	 */
	var validate = function(item) {
		if (!Validation.notNull(dialog, userName, userName.val(), '请输入用户名称')) {
			return false;
		}
		if (!Validation.notNull(dialog, userAccount, userAccount.val(), '请输入用户账号')) {
			return false;
		}
		if (!Validation.checkByRegExp(dialog, userAccount, '^[0-9a-zA-Z]*$', userAccount.val(), '用户帐号只能输入字母及数字')) {
			return false;
		}
		return true;
	};
	/*
	 *获取表单数据
	 */
	var getAllData = function(item) {
		var data = {};
		data['name'] = userName.val();
		data['userAccount'] = userAccount.val();
		data['description'] = description.val();
        //组织关联
        data['employeeId'] = employeeId.val();
		if (item) {
			data['id'] = item.id;
		}
		return data;
	};

    /**
     * 选择员工
     */
    var selectEmployees = function() {
        $.get(contextPath + '/pages/auth/employee-select.jsp').done(function(data) {
            var dialog = $(data);
            //显示对话框数据
            dialog.modal({
                keyboard: false,
                backdrop: false // 指定当前页为静态页面，如果不写后台页面就是黑色。
            }).on({
                'hidden.bs.modal': function(){
                    $(this).remove();
                },
                'shown.bs.modal': function(){
                    var columns = [
                        { title:'姓名', name:'name' , width: 100},
                        { title:'员工编号', name:'sn', width: 100},
                        { title:'性别', name:'gender', width: 60,
                            render: function(item, name, index){
                                if(item[name] == 'FEMALE'){
                                    return '女';
                                }else{
                                    return '男';
                                }
                            }
                        },
                        { title:'入职日期', name:'entryDate', width: 100},
                        { title:'所属机构', name:'organizationName', width: 210},
                        { title:'岗位', name:'postName', width: 180},
                        { title:'兼职岗位', name:'additionalPostNames', width: 180}
                    ];<!-- definition columns end -->

                    //查询出当前表单所需要得数据。
                    dialog.find('.selectEmployeeGrid').grid({
                        identity: 'id',
                        columns: columns,
                        url: contextPath + '/employee/pagingquery.koala'
                    });

                }
            });<!-- 显示对话框数据结束-->

            dialog.find('#selectEmployeeGridSave').on('click',function(){
                var items = dialog.find('.selectEmployeeGrid').data('koala.grid').selectedRows();

                if(items.length == 0){
                    dialog.find('.selectEmployeeGrid').message({
                        type: 'warning',
                        content: '请选择需要关联的员工！'
                    });
                }

                if(items.length>1) {
                    dialog.find('.selectEmployeeGrid').message({
                        type: 'warning',
                        content: '只能选择一个需要关联的员工！'
                    });

                   }
                var employeeId =  items[0].id;
                var employeeName = items[0].name;
                dialog.modal('hide');
                if(employeeId != null){
                    orgEmployee.find('[data-toggle="item"]').html(employeeName);
                }
                orgEmployee.find('input').val(employeeId);
                dialog.trigger('keydown');
            });

            //兼容IE8 IE9
            if(window.ActiveXObject){
                if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                    dialog.trigger('shown.bs.modal');
                }
            }

        });
    };<!-- 选择员工结束-->

	return {
		add : add,
		modify : modify,
		deleteUser : deleteUser,
		resetPassword : resetPassword,
		available : available,
		forbidden : forbidden
	};
};



