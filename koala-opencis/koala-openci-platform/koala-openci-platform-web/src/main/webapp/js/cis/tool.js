var toolManager = {

	baseUrl : 'tracconfiguration/',
	dataGrid : null,
	name : null,
	serviceUrl : null,
	username : null,
	password : null,
	savePath : null,
	token : null,
	toolType : null,
	requestRootAddress: null,
	email: null,
	dialog : null,
	developerId : null,

	add : function(toolType, grid) {
		var self = this;
		self.dataGrid = grid;
		self.toolType = toolType;
		self.baseUrl = self.getBaseUrl(toolType);
		$.get('pages/cis/tool-template.html').done(function(data) {
			self.init(data);
		});
	},

	update : function(toolType, grid, item) {
		var self = this;
		self.dataGrid = grid;
		self.toolType = toolType;
		self.baseUrl = self.getBaseUrl(toolType);
		$.get('pages/cis/tool-template.html').done(function(data) {
			self.init(data, item);
			self.setData(item)
		});
	},

	del : function(toolType, grid, items) {
		var self = this;
		self.toolType = toolType;
		self.baseUrl = self.getBaseUrl(toolType);
		$.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			'type' : "post",
			'url' : self.baseUrl + 'abolish',
			'data' : JSON.stringify(items[0]),
			'dataType' : 'json'
		}).done(function(data) {
			if (data.result) {
				self.dataGrid.message({
					type : 'success',
					content : '删除成功'
				});
				$(this).modal('hide');
				grid.grid('refresh');
			} else {
				self.dialog.find('.modal-content').message({
					type : 'error',
					content : data.actionError
				});
			}
		});
	},
	/**
	 * 初始化
	 */
	init : function(data, item) {
		var self = this;
		var dialog = $(data);
		self.dialog = dialog;
		dialog.find('.modal-header').find('.modal-title').html( item ? '修改工具配置' : '添加工具');
		self.name = dialog.find('#name');
		self.serviceUrl = dialog.find('#serviceUrl');
		self.username = dialog.find('#username');
		self.password = dialog.find('#password');
		self.savePath = dialog.find('#savePath');
		self.token = dialog.find('#token');
		self.email = dialog.find('#email');
		self.requestRootAddress = dialog.find('#requestRootAddress');
		if (self.toolType != 'TRAC' && self.toolType != 'SVN') {
			self.savePath.closest('.form-group').hide();
		}
		if (self.toolType != 'GIT' && self.toolType != 'JENKINS') {
			self.token.closest('.form-group').hide();
			
		}
		if (self.toolType != 'GIT'){
			self.email.closest('.form-group').hide();
		}
		if(self.toolType == 'JENKINS'){
			self.password.closest('.form-group').hide();
		}
		if(self.toolType != 'SVN'){
			self.requestRootAddress.closest('.form-group').hide();
		}
		dialog.find('#save').on('click', function() {
			self.save(item);
		}).end().modal({
			keyboard : false
		}).on({
			'hidden.bs.modal' : function() {
				$(this).remove();
			},
			'complete' : function() {
				self.dataGrid.message({
					type : 'success',
					content : '保存成功'
				});
				$(this).modal('hide');
				self.dataGrid.grid('refresh');
			}
		});
	},

	setData : function(item) {
		var self = this;
		self.name.val(item.name);
		self.serviceUrl.val(item.serviceUrl);
		self.username.val(item.username);
		self.password.val(item.password);
		if (item.savePath) {
			self.savePath.val(item.savePath);
		}
		if (item.token) {
			self.token.val(item.token);
		}
		if(self.toolType == 'JENKINS'){
			self.token.val(item.password);
		}
		if(self.toolType == 'SVN'){
			self.requestRootAddress.val(item.requestRootAddress);
		}
		if(self.toolType == 'GIT'){
			self.email.val(item.email);
		}
	},
	/*
	 *   保存数据 id存在则为修改 否则为新增
	 */
	save : function(item) {
		var self = this;
		if (!self.validate()) {
			return false;
		}
		var url = self.baseUrl + 'create';
		if (item) {
			url = self.baseUrl + 'update';
		}
		$.post(url, self.getAllData(item)).done(function(data) {
			if (data.result) {
				self.dialog.trigger('complete');
			} else {
				self.dataGrid.message({
					type : 'error',
					content : data.actionError
				});
			}
		});
	},
	/**
	 * 数据验证
	 */
	validate : function() {
		var self = this;
		var dialog = self.dialog;
		var name = self.name;
		var toolType = self.toolType;
		var serviceUrl = self.serviceUrl;
		var username = self.username;
		var password = self.password;
		var savePath = self.savePath;
		var token = self.token;
		var email = self.email;
		var requestRootAddress = self.requestRootAddress;
		if (!Validation.notNull(dialog, name, name.val(), '请输入工具名')) {
			return false;
		}
		if (!Validation.notNull(dialog, serviceUrl, serviceUrl.val(), '请输入工具地址')) {
			return false;
		}
		if (self.toolType == 'TRAC' || self.toolType == 'SVN') {
			if (!Validation.notNull(dialog, savePath, savePath.val(), '请输入保存地址')) {
				return false;
			}
		}
		if (self.toolType == 'GIT') {
			if (!Validation.notNull(dialog, token, token.val(), '请输入Token')) {
				return false;
			}
			if (!Validation.email(dialog, email, email.val(), '邮箱不合法')) {
				return false;
			}
		}
		if(self.toolType == 'SVN'){
			if (!Validation.notNull(dialog, requestRootAddress, requestRootAddress.val(), '请输入请求根路径')) {
				return false;
			}
		}
		if (!Validation.notNull(dialog, username, username.val(), '请输入用户名')) {
			return false;
		}
		if (self.toolType == 'JENKINS'){
			if (!Validation.notNull(dialog, token, token.val(), '请输入Tooken')) {
				return false;
			}
		}else{
			if (!Validation.notNull(dialog, password, password.val(), '请输入密码')) {
				return false;
			}
		}
		return true;
	},
	/*
	 *获取表单数据
	 */
	getAllData : function(item) {
		var self = this;
		var data = {};
		if (item) {
			data = item;
		}
		data['name'] = self.name.val();
		data['serviceUrl'] = self.serviceUrl.val();
		data['username'] = self.username.val();
		data['password'] = self.password.val();
		if (self.toolType == 'TRAC' || self.toolType == 'SVN') {
			data['savePath'] = self.savePath.val();
		}
		if (self.toolType == 'GIT') {
			data['token'] = self.token.val();
			data['email'] = self.email.val();
		}
		if(self.toolType == 'JENKINS'){
			data['password'] = self.token.val();
		}
		if(self.toolType == 'SVN'){
			data['requestRootAddress'] = self.requestRootAddress.val();
		}
		return data;
	},

	getBaseUrl : function(toolType) {
		switch(toolType) {
			case 'TRAC':
				return 'tracconfiguration/';
			case 'JIRA':
				return 'jirasconfiguration/';
			case 'SONAR':
				return 'sonarconfiguration/';
			case 'JENKINS':
				return 'jenkinsconfiguration/';
			case 'GIT':
				return 'gitconfiguration/';
			case 'SVN':
				return 'svnconfiguration/';
			default:
				return 'tracconfiguration/';
		}
	},
	
	testConnection: function(id, index){
		var self = this;
		$('#toolGrid').find('#usable'+index)
					.removeClass('glyphicon-remove')
					.addClass('glyphicon-ok')
					.css('color', '#5CB85C');
		$.get('toolconfiguration/can-connect/'+id).done(function(data){
			if(data.result){
				self.dataGrid.message({
						type: 'success',
						content: '该工具连接可用'
				});
				$('#toolGrid').find('#usable'+index)
					.removeClass('glyphicon-remove')
					.addClass('glyphicon-ok')
					.css('color', '#5CB85C');
			}else{
				self.dataGrid.message({
					type: 'error',
					content: data.actionError
				});
				$('#toolGrid').find('#usable'+index)
					.removeClass('glyphicon-ok')
					.addClass('glyphicon-remove')
					.css('color', '#D9534F');
			}
		});
	}
}
