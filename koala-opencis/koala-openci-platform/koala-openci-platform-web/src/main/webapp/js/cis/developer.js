var developerManager = {
	
	baseUrl: 'developer/',
	dataGrid: null,
	name: null,
	password: null,
	email: null,
	dialog: null,
	developerId: null,
	
	add: function(grid){
		var self = this;
		self.dataGrid = grid;
		$.get('pages/cis/developer-template.html').done(function(data){
			self.init(data);
		});
	},
	
	update: function(grid, item){
		var self = this;
		self.dataGrid = grid;
		$.get('pages/cis/developer-template.html').done(function(data){
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
		    'url': self.baseUrl + 'abolish_developers',
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
		self.password = dialog.find('#password');
        self.email = dialog.find('#email');
        self.developerId = dialog.find('#developerId');
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
		self.developerId.val(item.developerId);
		self.name.val(item.name);
		self.password.val(item.password);
		self.email.val(item.email);
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
		var password = self.password;
		var email = self.email;
		var developerId = self.developerId;
		if(!Validation.notNull(dialog, developerId, developerId.val(), '请输入开发者ID')){
			return false;
		}
		if(!Validation.notNull(dialog, name, name.val(), '请输入用户名称')){
			return false;
		}
		if(!Validation.notNull(dialog, password, password.val(), '请输入用户密码')){
			return false;
		}
		if(password.val().length < 8){
			showErrorMessage(dialog, password, '密码长度至少为8位');
			return false;
		}
		if(!Validation.notNull(dialog, email, email.val(), '请输入邮箱')){
			return false;
		}
		if(!Validation.email(dialog, email, email.val(), '邮箱格式不正确')){
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
		data['developerId'] = self.developerId.val();
		data['name'] = self.name.val();
		data['password'] = self.password.val();
		data['email'] = self.email.val();
		if(item){
			data['id'] = item.id;	
		}
		return data;
	}
}
