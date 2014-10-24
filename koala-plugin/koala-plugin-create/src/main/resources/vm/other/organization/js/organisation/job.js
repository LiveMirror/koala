var job = function(){
	var baseUrl = contextPath + '/job/';
	var dialog = null;    			//对话框
	var jobName = null;   			//职务名称
	var jobSn = null;    			//职务编号
	var jobDescription = null;    	//职务描述
	var version = null;    			//version字段
	var job = null;
	var dataGrid = null; 			//Grid对象
	/*
	 *新增 
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get( contextPath + '/pages/organisation/job-template.jsp').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(id, grid){
		dataGrid = grid;
		$.get( contextPath + '/pages/organisation/job-template.jsp').done(function(data){
			init(data, id);
			setData(id);
		});
	};
	/*
	 删除方法
	 */
	var remove = function(objects, grid){
		dataGrid = grid;
		
		for (var i = 0; i < objects.length; i++) {
		    delete objects[i]["new"];
		}
		
		$.ajax({
		    headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
		    'type': "Post",
		    'url': baseUrl + 'terminate-jobs.koala',
		    'data': JSON.stringify(objects),
		    'dataType': 'json'
		 }).done(function(data){
			if(data.success){
				dataGrid.message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				dataGrid.message({
					type: 'error',
					content: data.errorMessage
				});
			}
		}).fail(function(data){
			dataGrid.message({
				type: 'error',
				content: '删除失败'
			});
		});
	};
	/**
	 * 初始化
	 */
	var init = function(data, id){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(id ? '修改职务信息':'创建职务');
		jobName = dialog.find('#jobName');
		jobSn = dialog.find('#jobSn');
		jobDescription = dialog.find('#jobDescription');
		dialog.find('#save').on('click',function(){
			$(this).attr('disabled', 'disabled');
			save(id);
		}).end().modal({
			keyboard: false
		}).on({
			'hidden.bs.modal': function(){
				$(this).remove();
			},
			'complete': function(){
				dataGrid.message({
					type: 'success',
					content: '保存成功'
				});
				$(this).modal('hide');
				dataGrid.grid('refresh');
			}
		});
	};
	/*
	 *设置值
	 */
	var setData = function(id){
		$.get(baseUrl+'get/'+id+'.koala').done(function(result){
			job = result;
			jobName.val(job.name);
			jobSn.val(job.sn);
			jobDescription.val(job.description);
			version = job.version;
		});
	};
	/*
	*   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(id){
		if(!validate()){
			dialog.find('#save').removeAttr('disabled');
			return false;
		}
		var url = baseUrl + 'create.koala';
		if(id){
			url =  baseUrl + 'update.koala?id='+id;
		}
		$.post(url, getAllData(id)).done(function(data){
			if(data.success){
				dialog.trigger('complete');
			} else if(data.errorMessage == '该职务已存在'){
				showErrorMessage(dataSourceId, '该职务已存在');
			} else {
				dialog.find('.modal-content').message({
					type: 'error',
					content: data.errorMessage
				});
			}
			dialog.find('#save').removeAttr('disabled');	
			refreshToken(dialog.find('input[name="koala.token"]'));
		});
	};
	/*
	*获取表单数据
	 */
	var getAllData = function(id){
		if(id){
			job.name = jobName.val();
			job.sn = jobSn.val();
			job.description = jobDescription.val();
			if (version != null) {
				job.version = version;
			}
			job['koala.token'] = dialog.find('input[name="koala.token"]').val();
			return job;
		}else{
			var data = {};
			data.name = jobName.val();
			data.sn = jobSn.val();
			data.description = jobDescription.val();
			if (version != null) {
				data.version = version;
			}
			data['koala.token'] = dialog.find('input[name="koala.token"]').val();
			return data;
		}
	};
	/**
	 * 表单验证 通过返回true  失败返回false
	 */
	var validate = function(){
		if(!Validation.notNull(dialog, jobSn, jobSn.val(), '请输入职务编号！')){
			return false;
		}
		if(!Validation.serialNmuber(dialog, jobSn, jobSn.val(), '请填写正确的职务编号！')){
			return false;
		}
		if(!Validation.notNull(dialog, jobName, jobName.val(), '请输入职务名称！')){
			return false;
		}
		return true;
	};
	/**
	 * 显示提示信息
	 */
	var showErrorMessage = function($element, content){
		$element.popover({
			content: content,
			trigger: 'manual',
			container: dialog
		}).popover('show').on({
			'blur': function(){
				$element.popover('destroy');
				$element.parent().removeClass('has-error');
			},
			'keydown': function(){
				$element.popover('destroy');
				$element.parent().removeClass('has-error');
			}
		}).focus().parent().addClass('has-error');
	};
	return {
		add: add,
		modify: modify,
		del: remove
	};
};