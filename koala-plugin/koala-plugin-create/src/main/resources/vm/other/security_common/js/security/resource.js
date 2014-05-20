var resourceManager = function(){
	var baseUrl = contextPath + '/auth/Resource/';
	var dialog = null;    //对话框
	var parentName = null;   //父资源名称
	var name = null; //资源名称
	var identifier = null; //资源标识
	var resourceType = null; //资源类型
	var desc = null; //资源描述
	var dataGrid = null; //Grid对象
	var opreate = null;
	var parentId = null;
	var parentLevel = null;
	/*
	 *新增
	 */
	var add = function(grid, item){
		dataGrid = grid;
		opreate = 'add';
		$.get(contextPath + '/pages/auth/resource-template.jsp').done(function(data){
			if(item){
				init(data);
				parentId = item.id;
				parentLevel = item.level;
				parentName.val(item.name);
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
		$.get(contextPath + '/pages/auth/resource-template.jsp').done(function(data){
			init(data,item);
		});
	};
	/*
	 删除方法
	 */
	var deleteItem = function(resource, grid){
		var data = {};
		data['resourceVO.id'] = resource.id;
		data['resourceVO.identifier'] = resource.identifier;
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
		
		opreate ? dialog.addClass(opreate) : null;
		
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改资源信息':'添加资源');
		parentName = dialog.find('#parentName');
		name = dialog.find('#name');
		identifier = dialog.find('#identifier');
		resourceType = dialog.find('#resourceType');
		desc = dialog.find('#desc');
		$.get(baseUrl+'findAllResourceType.koala').done(function(result){
			var content = new Array();
			for(var i=0,j=result.data.length; i<j; i++){
				var obj = result.data[i];
				content.push({title: obj.name, value: obj.id});
			}
			resourceType.select({
				title: '选择资源类型',
				contents: content
			});
			if(opreate == 'modify'){
				setData(item);
			}
		});
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
		parentName.closest('.form-group').hide();
		name.val(item.name);
		identifier.val(item.identifier);
		desc.val(item.desc);
		resourceType.setValue(item.typeId)
	}
		
	/*
	*   保存数据 id存在则为修改 否则为新增
	 */
	var save = function(item){
		if(!validate(item)){
			dialog.find('#save').removeAttr('disabled');			
			return false;
		}
		var url = baseUrl + 'add.koala';
		if(item){
			url =  baseUrl + 'update.koala';
		}
		if(parentId && parentLevel){
			url =  baseUrl + 'addAndAssignParent.koala';
		}
		$.post(url,getAllData(item)).done(function(data){
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
	var validate = function(item){
		if(!Validation.notNull(dialog, name, name.val(), '请输入资源名称')){
			return false;
		}
		if(!Validation.notNull(dialog, identifier, identifier.val(), '请输入资源标识')){
			return false;
		}
		if(!Validation.notNull(dialog, resourceType, resourceType.getValue(), '请选择资源类型')){
			return false;
		}
		return true;
	}
	/*
	*获取表单数据
	 */
	var getAllData = function(item){
		var data = {};
		if(item){
			data['resourceVO.id'] = item.id;	
		}
		if(parentId && parentLevel){
			data['childVO.desc'] = desc.val();
			data['childVO.identifier'] = identifier.val();
			data['childVO.name'] = name.val();
			data['childVO.typeId'] = resourceType.getValue();
			data['parentVO.id'] = parentId;
			data['parentVO.level'] = parentLevel;
		}else{
			data['resourceVO.desc'] = desc.val();
			data['resourceVO.identifier'] = identifier.val();
			data['resourceVO.name'] = name.val();
			data['resourceVO.typeId'] = resourceType.getValue();
		}
		data['koala.token'] = dialog.find('input[name="koala.token"]').val();
		return data;
	};
	return {
		add: add,
		modify: modify,
		deleteItem: deleteItem
	};
};