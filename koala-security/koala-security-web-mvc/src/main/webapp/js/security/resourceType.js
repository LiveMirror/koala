var resourceTypeManager = function(){
	var baseUrl = contextPath + '/auth/ResourceType/';
	var dialog = null;    //对话框
	var resourceTypeName = null;   //资源类型名称
	var dataGrid = null; //Grid对象
	/*
	 *新增
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/resource-type-template.jsp').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(item, grid){
		dataGrid = grid;
		$.get(contextPath + '/pages/auth/resource-type-template.jsp').done(function(data){
			init(data,item);
			setData(item);
		});
	};
	/*
	 删除方法
	 */
	var deleteItem = function(resourceTypes, grid){
		var data = {};
		for(var i=0,j=resourceTypes.length; i<j; i++){
			var resourceType = resourceTypes[i];
			data['resourceTypeVOs['+i+'].id'] = resourceType.id;
		}
		dataGrid = grid;
		$.post(baseUrl + 'delete.koala', data).done(function(data){
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
	var init = function(data, item){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改资源类型':'添加资源类型');
		resourceTypeName = dialog.find('#resourceTypeName');
		dialog.find('#save').on('click',function(){
			$(this).attr('disabled', 'disabled');			
			save(item);
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
	var setData = function(item){
		resourceTypeName.val(item.name);
	}
		
	/*
	*   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(item){
		if(!validate(item)){
			dialog.find('#save').removeAttr('disabled');			
			return false;
		}
		var url = baseUrl + 'save.koala';
		if(item){
			url =  baseUrl + 'update.koala';
		}
		$.post(url,getAllData(item)).done(function(data){
			if(data.result == 'success'){
				dialog.trigger('complete');
			}else{
				dialog.find('.modal-content').message({
					type: 'error',
					content: data.actionError
				});
			}
			dialog.find('#save').removeAttr('disabled');			
		});
	};
	/**
	 * 数据验证
	 */
	var validate = function(item){
		if(!Validation.notNull(dialog, resourceTypeName, resourceTypeName.val(), '请输入资源类型名称')){
			return false;
		}
		return true;
	}
	/*
	*获取表单数据
	 */
	var getAllData = function(item){
		var data = {};
		data['resourceTypeVO.name'] = resourceTypeName.val();
		data['koala.token'] = dialog.find('input[name="koala.token"]').val();
		if(item){
			data['resourceTypeVO.id'] = item.id;	
		}
		return data;
	};
	return {
		add: add,
		modify: modify,
		deleteItem: deleteItem
	};
};