var roleManager = {
	
	baseUrl: 'role/',
	dataGrid: null,
	name: null,
	description: null,
	dialog: null,
	developerId: null,
	
	add: function(grid){
		var self = this;
		self.dataGrid = grid;
		$.get('pages/cis/role-template.html').done(function(data){
			self.init(data);
		});
	},
	
	update: function(grid, item){
		var self = this;
		self.dataGrid = grid;
		$.get('pages/cis/role-template.html').done(function(data){
			self.init(data, item);
			self.setData(item)
		});
	},
	
	del: function(grid, items){
		var self = this;
		$.each(items, function(){
			delete this['new'];
		})
		$.ajax({
		    headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
		    'type': "Post",
		    'url': self.baseUrl + 'abolishs',
		    'data': JSON.stringify(items),
		    'dataType': 'json'
		 }).done(function(data){
			if(data.result){
				self.dataGrid.message({
						type: 'success',
						content: '删除成功'
					});
					$(this).modal('hide');
					grid.grid('refresh');
			}else{
				self.dialog.find('.modal-content').message({
					type: 'error',
					content: data.actionError
				});
			}
		});
	},
	/**
	 * 初始化
	 */
	init: function(data, item){
		var self = this;
		var dialog = $(data);
		self.dialog = dialog;
		dialog.find('.modal-header').find('.modal-title').html(item ? '修改开发者信息':'添加开发者');
		self.name = dialog.find('#name');
        self.description = dialog.find('#description');
		dialog.find('#save').on('click',function(){
			self.save(item);
		}).end().modal({
			keyboard: false
		}).on({
				'hidden.bs.modal': function(){
					$(this).remove();
				},
				'complete': function(){
					self.dataGrid.message({
						type: 'success',
						content: '保存成功'
					});
					$(this).modal('hide');
					self.dataGrid.grid('refresh');
				}
		});
	},
	
	setData: function(item){
		var self = this;
		self.description.val(item.description);
		self.name.val(item.name);
	},
	/*
	*   保存数据 id存在则为修改 否则为新增
	 */
	save: function(item){
		var self = this;
		if(!self.validate()){
			return false;
		}
		var url = self.baseUrl + 'create';
		if(item){
			url =  self.baseUrl + 'update';
		}
		$.post(url, self.getAllData(item)).done(function(data){
			if(data.result){
				self.dialog.trigger('complete');
			}else{
				self.dialog.find('.modal-content').message({
					type: 'error',
					content: data.actionError
				});
			}
		});
	},
	/**
	 * 数据验证
	 */
	validate: function(){
		var self = this;
		var dialog = self.dialog;
		var name = self.name;
		if(!Validation.notNull(dialog, name, name.val(), '请输入角色名')){
			return false;
		}
		return true;
	},
	/*
	*获取表单数据
	 */
	getAllData: function(item){
		var self = this;
		var data = {};
		if(item){
			data = item;
		}
		data['description'] = self.description.val();
		data['name'] = self.name.val();
		if(item){
			data['id'] = item.id;	
		}
		return data;
	}
}
