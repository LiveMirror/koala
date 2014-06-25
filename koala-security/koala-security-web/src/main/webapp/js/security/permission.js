/**
 * 这个js文件和其他同类的js实现方法有异，
 * 去除一些繁杂的设计和实现，敬请留意
 */
var menuManager = function(){
	var baseUrl 	= contextPath + '/auth/menu/';
	var dialog 		= null;	//对话框
	
	var dataGrid 	= null; //Grid对象
	var opreate 	= null;
	
	/**
	 * 新增
	 */
	var add = function(grid, item){
		dataGrid = grid;
		opreate = 'add';
		$.get(contextPath + '/pages/auth/permission-template.jsp').done(function(data){
			if(item){
				init(data);
			}else{
				init(data,null,opreate);
			}
		});
	};
	/*
	 * 修改
	 */
	var modify = function(item, grid){
		dataGrid = grid;
		opreate = 'modify';
		$.get(contextPath + '/pages/auth/permission-template.jsp').done(function(data){
			init(data,item,opreate);
		});
	};
	
	/*
	 删除方法
	 */
	var deleteItem = function(resource, grid){
		var data = {};
		data['id'] = resource.id;
		dataGrid = grid;
		$.post(baseUrl + 'del.koala', data).done(function(data){
			if(data.result == 'success'){
				dataGrid.message({
					type: 'success',
					content: '删除成功'
				});
				dataGrid.grid('refresh');
			}else{
				dataGrid.message({
					type: 'error',
					content: data.actionError
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
	var init = function(data, item, opreate){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改权限信息':'添加权限');
		
		var form = dialog.find("#permisstion_form");
		
		if(opreate == 'modify'){
			form.find("input[name='permissionName']").val(item.permissionName);
			form.find("input[name='identifier']").val(item.identifier);
			form.find("input[name='description']").val(item.description);
		}

		dialog.find('#save').on('click',function(){
			$(this).attr('disabled', 'disabled');
			save(item,form);
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
	*   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(item ,form){
		if(!validate(form.find("input[name='permissionName']"))){
			dialog.find('#save').removeAttr('disabled');			
			return false;
		}
		var data = form.serialize();
		var url = baseUrl + 'add.koala';
		
		console.log(item);
		
		if(item){
			data.permissionId = item.permissionId;
			url =  baseUrl + 'update.koala';
		}
		
		$.post(url,data).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete');
			}else{
				dialog.find('.modal-content').message({
					type: 'error',
					content: data.actionError
				});
				refreshToken(dialog.find('input[name="koala.token"]'));
			}
			dialog.find('#save').removeAttr('disabled');
		});
	};
	/**
	 * 数据验证
	 */
	var validate = function(name){
		if(!Validation.notNull(dialog, name, name.val(), '请输入权限名称')){
			return false;
		}
		return true;
	};

	return {
		add			: add,
		modify		: modify,
		deleteItem	: deleteItem
	};
};