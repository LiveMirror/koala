var moduleManager = {

	baseUrl : 'project/',
	dataGrid : null,
	project : null,
	moduleName : null,
	basePackage : null,
	moduleType : null,
	projectName : null,
	functionsGrid : null,
	dependenciesGrid : null,
	dialog : null,

	add : function(dataGrid) {
		var self = this;
		self.dataGrid = dataGrid;
		self.projectName = projectDto.projectName;
		self.project = projectDto.projectForCreate;
		$.get('pages/cis/module-template.html').done(function(data) {
			self.init(data);
		});
	},
	update : function(dataGrid, item) {
		var self = this;
		self.dataGrid = dataGrid;
		self.projectName = projectDto.projectName;
		self.project = projectDto.projectForCreate;
		$.get('pages/cis/module-template.html').done(function(data) {
			self.init(data, item);
			self.setData(item)
		});
	},
	del : function(dataGrid, indexs) {
		var grid = dataGrid.getGrid();
		grid.removeRows(indexs);
		projectDto.projectForCreate.module = grid.getAllItems();
		$('body').message({
			type : 'success',
			content : '删除成功'
		});
	},
	/**
	 * 初始化
	 */
	init : function(data, item) {
		var self = this;
		var dialog = $(data);
		self.dialog = dialog;
		dialog.find('.modal-header').find('.modal-title').html( item ? '添加模块' : '添加模块信息');
		self.moduleName = dialog.find('#moduleName');
		self.basePackage = dialog.find('#basePackage');
		self.moduleType = dialog.find('#moduleType');
		self.functionsGrid = dialog.find('#functionsGrid');
		self.dependenciesGrid = dialog.find('#dependenciesGrid');
		dialog.find('#save').on('click', function() {
			self.save(item);
		}).end().modal({
			keyboard : false
		}).on({
			'hidden.bs.modal' : function() {
				$(this).remove();
			},
			'shown.bs.modal' : function() {
				self.initModualSelect(item);
			},
			'complete' : function() {
				$('body').message({
					type : 'success',
					content : '保存成功'
				});
				$(this).modal('hide');
				self.dataGrid.grid('refresh');
			}
		});
		//兼容IE8 IE9
		if (window.ActiveXObject) {
			if (parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10) {
				dialog.trigger('shown.bs.modal');
			}
		}
	},
	initModualSelect : function(item) {
		var self = this;
		self.moduleType.select({
			title : '请选择',
			contents : [{
				title : '基础实施层',
				value : 'infra'
			}, {
				title : '领域层',
				value : 'bizModel'
			}, {
				title : '应用层接口',
				value : 'applicationInterface'
			}, {
				title : '应用层实现',
				value : 'applicationImpl'
			}, {
				title : '视图层',
				value : 'war'
			}]
		}).on('change', function() {
			self.initGrid($(this).getValue(), item);
		}).setValue( item ? item.moduleType : 'infra');
	},

	initGrid : function(moduleType, item) {
		var self = this;
		$.get(self.baseUrl + 'get-functions?moduleType=' + moduleType).done(function(data) {
			var functions = [];
			for (functionName in data.functions) {
				functions.push({
					functionName : functionName,
					functionDesc : data.functions[functionName]
				});
			}
			var columns = [{
				title : '功能名称',
				name : 'functionName',
				width : 150
			}, {
				title : '功能描述',
				name : 'functionDesc',
				width : 150
			}];
			self.functionsGrid.empty().data('koala.grid', null).grid({
				identity : 'functionName',
				columns : columns,
				isShowPages : false,
				isUserLocalData : true,
				localData : functions
			}).on('complate', function() {
				var self = $(this);
				if (item && item.functions) {
					$.each(item.functions, function() {
						self.find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + this + '"]').addClass('checked');
					});
				}
			});
		});
		var param = {};
		var project = self.project;
		delete project.scanPackages;
		delete project.packageName;
		delete project.groupPackage;
		if (project.module) {
			for (var i = 0, j = project.module.length; i < j; i++) {
				delete project.module[i].security;
				delete project.module[i].basePackagePath;
			}
		}
		$.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			'type' : "Post",
			'url' : self.baseUrl + 'get-dependables?moduleType=' + moduleType,
			'data' : JSON.stringify(project),
			'dataType' : 'json'
		}).done(function(data) {
			var dependencies = [];
			$.each(data, function() {
				dependencies.push({
					moduleName : this.moduleName,
					moduleType : this.moduleType
				})
			});
			var columns = [{
				title : '模块名称',
				name : 'moduleName',
				width : 150
			}, {
				title : '模块类型',
				name : 'moduleType',
				width : 150,
				render : function(item, name, index) {
					switch(item[name]) {
						case 'infra':
							return '基础实施层';
							break;
						case 'bizModel':
							return '领域层';
							break;
						case 'applicationInterface':
							return '应用层接口';
							break;
						case 'applicationImpl':
							return '应用层实现';
							break;
						case 'war':
							return '视图层';
							break;
						default:
							return '';
					}
				}
			}];
			self.dependenciesGrid.empty().data('koala.grid', null).grid({
				identity : 'moduleName',
				columns : columns,
				isShowPages : false,
				isUserLocalData : true,
				localData : dependencies
			}).on('complate', function() {
				var self = $(this);
				if (item && item.dependencies) {
					$.each(item.dependencies, function() {
						self.find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + this + '"]').addClass('checked');
					});
				}
			});
		});
	},

	setData : function(item) {
		var self = this;
		self.basePackage.val(item.basePackage);
		self.moduleName.attr('disabled', true).val(item.moduleName);
	},
	/*
	 *   保存数据 id存在则为修改 否则为新增
	 */
	save : function(item) {
		var self = this;
		if (!self.validate()) {
			return false;
		}
		module = {};
		if (item) {
			module = item;
		}
		module.moduleName = self.moduleName.val();
		module.basePackage = self.basePackage.val();
		module.moduleType = self.moduleType.getValue();
		module.projectName = self.projectName;
		module.functions = [];
		module.dependencies = [];
		$.each(self.functionsGrid.getGrid().selectedRows(), function() {
			module.functions.push(this.functionName);
		});
		$.each(self.dependenciesGrid.getGrid().selectedRows(), function() {
			module.dependencies.push(this.moduleName);
		});
		var grid = self.dataGrid.getGrid();
		console.info(item)
		if (item) {
			grid.updateRows(item.moduleName, item);
		} else {
			grid.insertRows(module);
		}
		$('body').message({
			type : 'success',
			content : '保存成功'
		});
		self.dialog.modal('hide');
		projectDto.projectForCreate.module = grid.getAllItems();
	},
	/**
	 * 数据验证
	 */
	validate : function() {
		var self = this;
		var dialog = self.dialog;
		var moduleName = self.moduleName;
		var basePackage = self.basePackage;
		var moduleType = self.moduleType;
		if (!Validation.notNull(dialog, moduleName, moduleName.val(), '请输入模块名称')) {
			return false;
		}
		if (!Validation.notNull(dialog, basePackage, basePackage.val(), '请输入包路径')) {
			return false;
		}
		if (!Validation.notNull(dialog, moduleType, moduleType.getValue(), '请选择模块类型')) {
			return false;
		}
		return true;
	}
}
