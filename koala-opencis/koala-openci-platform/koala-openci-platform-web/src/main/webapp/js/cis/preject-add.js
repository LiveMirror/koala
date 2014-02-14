var projectDto = {};
$(function() {
	var flag = false;
	var projectAdd = $('.project-add');
	projectAdd.find('.items').width(5 * $('#content').width());

	projectAdd.wizard({
		totalSteps: 5
	}).on({
		'step0' : function() {
			var $this = $(this);
			var projectName = $this.find('#projectName').val();
			var groupId = $this.find('#groupId').val();
			var artifactId = $this.find('#artifactId').val();
			var version = $this.find('#version').val();
			var dbProtocol = $this.find('#dbProtocolValue').val();
			var mvcProtocol = $this.find('#mvcProtocolValue').val();
			projectDto.projectName = projectName;
			var projectForCreate = {};
			projectForCreate.groupId = groupId;
			projectForCreate.artifactId = artifactId;
			projectForCreate.version = version;
			projectForCreate.dbProtocol = dbProtocol;
			projectForCreate.mvcProtocol = mvcProtocol;
			projectDto.projectForCreate = projectForCreate;
			if (!flag) {
				initModuleGrid([]);
			}
		}
	});

	projectAdd.find('#projectName').on('keyup', function() {
		var projectName = $(this).val();
		if (projectName && projectName.length > 0) {
			projectAdd.find('#artifactId').val($(this).val());
		}
	});

	projectAdd.find('#dbProtocol').select({
		title : '请选择',
		contents : [{
			title : 'JPA',
			value : 'JPA',
			selected : true
		}, {
			title : 'Mybatis',
			value : 'Mybatiss'
		}]
	}).on('change', function() {
		$('#dbProtocolValue').val($(this).getValue());
	}).trigger('change');

	projectAdd.find('#mvcProtocol').select({
		title : '请选择',
		contents : [{
			title : 'Struts2MVC',
			value : 'Struts2MVC'
		}, {
			title : 'SpringMVC',
			value : 'SpringMVC',
			selected : true
		}]
	}).on('change', function() {
		$('#mvcProtocolValue').val($(this).getValue());
	}).trigger('change');

	//-------------模块选择------------------------------//
	var initModuleGrid = function(data) {
		var columns = [{
			title : '模块名称',
			name : 'moduleName',
			width : 150
		}, {
			title : '包路径',
			name : 'basePackage',
			width : 250
		}, {
			title : '模块类型',
			name : 'moduleType',
			width : 120,
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
		}, {
			title : '模块依赖',
			name : 'dependencies',
			width : 250,
			render : function(item, name, index) {
				return item[name].join(',');
			}
		}, {
			title : '功能依赖',
			name : 'functions',
			width : 'auto',
			render : function(item, name, index) {
				return item[name].join(',');
			}
		}];
		var buttons = [{
			content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button>',
			action : 'add'
		}, {
			content : '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button>',
			action : 'modify'
		}, {
			content : '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>删除</button>',
			action : 'delete'
		}, {
			content : '<button class="btn btn-info" type="button"><span class="glyphicon glyphicon-ok"><span>默认模块</button>',
			action : 'useDefaultModule'
		}];
		projectAdd.find('#modualGrid').off().empty().data('koala.grid', null).grid({
			identity : 'moduleName',
			buttons : buttons,
			columns : columns,
			isShowPages : false,
			isUserLocalData : true,
			localData : data
		}).on({
			'add' : function() {
				moduleManager.add($(this));
			},
			'modify' : function(e, data) {
				var indexs = data.data;
				var $this = $(this);
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择一条记录进行修改'
					});
					return;
				}
				if (indexs.length > 1) {
					$this.message({
						type : 'warning',
						content : '只能选择一条记录进行修改'
					});
					return;
				}
				moduleManager.update($this, data.item[0]);
			},
			'delete' : function(e, data) {
				var indexs = data.data;
				var $this = $(this)
				if (indexs.length == 0) {
					$this.message({
						type : 'warning',
						content : '请选择要操作的记录'
					});
					return;
				}
				$this.confirm({
					content : '确定要删除所选记录吗?',
					callBack : function() {
						moduleManager.del($this, indexs);
					}
				});
			},
			'useDefaultModule' : function() {
				var param = {};
				param['projectName'] = projectDto.projectName;
				param['projectForCreate.artifactId'] = projectDto.projectForCreate.artifactId;
				param['projectForCreate.dbProtocol'] = projectDto.projectForCreate.dbProtocol;
				param['projectForCreate.groupId'] = projectDto.projectForCreate.groupId;
				param['projectForCreate.mvcProtocol'] = projectDto.projectForCreate.mvcProtocol;
				param['projectForCreate.version'] = projectDto.projectForCreate.version;
				$.post('project/generate-default-modules', param).done(function(result) {
					projectDto = result;
					initModuleGrid(projectDto.projectForCreate.module);
				});
			}
		});
		flag = true;
	}
	//-------------子系统选择------------------------------//
	var selectSubSystem = projectAdd.find('.select-sub-system');
	selectSubSystem.find('.checker').on('click', function() {
		$(this).find('span').toggleClass('checked');
	});
	selectSubSystem.find('#cacheType').select({
		title : '请选择',
		contents : [{
			title : 'EhCache',
			value : 'ehcache',
			selected : true
		}, {
			title : 'Memcached',
			value : 'memcached'
		}]
	}).on('change', function() {
		$('#cacheTypeValue').val($(this).getValue());
	}).trigger('change');

	selectSubSystem.find('#monitorType').select({
		title : '请选择',
		contents : [{
			title : '本地',
			value : 'local',
			selected : true
		}, {
			title : '分布式',
			value : 'distributed'
		}]
	}).on('change', function() {
		$('#monitorTypeValue').val($(this).getValue());
	}).trigger('change');

	//-------------开发者选择------------------------------//
	var initDeveloperGrid = function(data) {
		projectAdd.find('#developerGrid').off().empty().data('koala.grid', null).grid({
			identity : 'id',
			columns : [{
				title : '开发者ID',
				name : 'developerId',
				width : 150
			}, {
				title : '用户名称',
				name : 'name',
				width : 150
			}, {
				title : '邮箱',
				name : 'email',
				width : 150
			}, {
				title : '角色',
				name : 'roles',
				width : 250,
				render: function(item, name, index){
					var roles = [];
					$.each(item[name], function() {
						roles.push(this.name)
					});
					return roles.join(',');
				}
			}, {
				title : '操作',
				name : 'id',
				width : 'auto',
				render : function(item, name, index) {
					return '<button class="btn btn-danger" type="button" onclick="deleteDeveloper(' + item[name] + ')">删除</button>';
				}
			}],
			buttons : [{
				content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>选择开发者</button>',
				action : 'selectDeveloper'
			}],
			isShowPages : false,
			isUserLocalData : true,
			localData : data
		}).on('selectDeveloper', function() {
			selectDeveloper($(this).getGrid().getAllItems());
		});
	}
	initDeveloperGrid([]);

	var selectDeveloper = function(items) {
		$.get('pages/cis/select-developer.html').done(function(data) {
			var dialog = $(data);
			dialog.modal({
				keyboard : false
			}).on({
				'hidden.bs.modal' : function() {
					$(this).remove();
				},
				'shown.bs.modal' : function() {
					initSelectDeveloperGrid(dialog, items);
				}
			});
			//兼容IE8 IE9
			if (window.ActiveXObject) {
				if (parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10) {
					dialog.trigger('shown.bs.modal');
				}
			}
		});
	}
	var initSelectDeveloperGrid = function(dialog, items) {
		var selectedDevelopers = {};
		if (items) {
			$.each(items, function() {
				selectedDevelopers[this.id] = this;
			});
		}
		$.get('role/findall').done(function(roles) {
			var rolesHtml = [];
			$.each(roles, function() {
				rolesHtml.push('<div class="checker role"><span data-value="'+this.id+'" data-name="'+this.name+'"></span><label>'+this.name+'</label></div>');
			});
			rolesHtmls = rolesHtml.join('');
			dialog.find('#developerGrid').grid({
				identity : 'id',
				columns : [{
					title : '开发者ID',
					name : 'developerId',
					width : 150
				}, {
					title : '用户名称',
					name : 'name',
					width : 150
				}, {
					title : '邮箱',
					name : 'email',
					width : 150
				}, {
					title : '角色',
					name : 'roles',
					width : 250,
					render : function() {
						return rolesHtmls;
					}
				}],
				querys : [{
					title : '开发者ID',
					value : 'developerId'
				}, {
					title : '姓名',
					value : 'name'
				}, {
					title : '邮箱',
					value : 'email'
				}],
				url : 'developer/pagingquery'
			}).on({
				'selectedRow' : function(e, result) {
					var data = result.item;
					if (result.checked) {
						selectedDevelopers[data.id] = data;
						selectedDevelopers[data.id].roles = {};
						var $tr = $(this).find('[data-role="indexCheckbox"][data-value="'+data.id+'"]').closest('tr');
						$tr.find('.role .checked').each(function(){
							var value = $(this).data('value');
							var name = $(this).data('name');
							selectedDevelopers[data.id].roles[value] = {id:value, name:name};
						});
					} else {
						delete selectedDevelopers[data.id];
					}
				},
				'complate' : function() {
					var $this = $(this);
					for (id in selectedDevelopers) {
						if(id == 'roles'){
							var $tr = $this.find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + id + '"]');
							for(roleId in selectedDevelopers[id]){
								$tr.find('.role span[data-value="'+roleId+'"]').addClass('checked');
							}
						}else{
							$this.find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + id + '"]').click();
						}
					}
					$this.find('.role').on('click', function(e){
						e.stopPropagation();
						e.preventDefault();
						var $span = $(this).find('span:first').toggleClass('checked');
						var $tr = $(this).closest('tr');
						if($tr.hasClass('success')){
							var value = $(this).find('span').data('value');
							var name = $(this).find('span').data('name');
							var id = $tr.find('[data-role="indexCheckbox"]').data('value');
							if($span.hasClass('checked')){
								selectedDevelopers[id].roles[value] = {id:value, name:name};
							}else{
								delete selectedDevelopers[id].roles[value]
							}
						}
					});
				}
			});
		});
		dialog.find('#save').on('click', function() {
			var developers = [];
			for (id in selectedDevelopers) {
				developers.push(selectedDevelopers[id])
			}
			initDeveloperGrid(developers);
			dialog.modal('hide');
		});
	}
	//-------------工具选择------------------------------//
	var jenkinsConfig = {};
	projectAdd.find('#isUseCas').on('click', function() {
		$(this).toggleClass('checked');
	});
	var initToolGrid = function(data) {
		projectAdd.find('#toolsGrid').off().empty().data('koala.grid', null).grid({
			identity : 'id',
			columns : [{
				title : '工具名',
				name : 'name',
				width : 250
			}, {
				title : '工具类型',
				name : 'toolType',
				width : 250
			}, {
				title : '工具地址',
				name : 'serviceUrl',
				width : 'auto'
			}, {
				title : '操作',
				name : 'id',
				width : 'auto',
				render : function(item, name, index) {
					return '<button class="btn btn-danger" type="button" onclick="deleteTool(' + item[name] + ')">删除</button>';
				}
			}],
			buttons : [{
				content : '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>选择工具</button>',
				action : 'selectTool'
			}],
			isShowPages : false,
			isUserLocalData : true,
			localData : data
		}).on({
			'selectTool' : function() {
				selectTool($(this).getGrid().getAllItems());
			},
			'complate' : function() {
				if ($(this).find('#isUseCas').length == 0) {
					$('<div class="row" style="margin-top:5px;"><div class="checker isUseCas" style="margin-left:15px;margin-right:2px"><span id="isUseCas"></span></div><div style="display: inline; top: 2px; position: relative;"><label class="control-label">是否整合CAS</label><div></div></div></div>').insertAfter($(this).find('.buttons')).find('#isUseCas').on('click', function() {
						$(this).toggleClass('checked');
					});
				}
			}
		});
	}
	initToolGrid([]);
	var selectTool = function(items) {
		$.get('pages/cis/select-tool.html').done(function(data) {
			var dialog = $(data);
			dialog.modal({
				keyboard : false
			}).on({
				'hidden.bs.modal' : function() {
					$(this).remove();
				},
				'shown.bs.modal' : function() {
					initSelectToolGrid(dialog, items);
				}
			});
			//兼容IE8 IE9
			if (window.ActiveXObject) {
				if (parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10) {
					dialog.trigger('shown.bs.modal');
				}
			}
		});
	}
	var initSelectToolGrid = function(dialog, items) {
		var selectedTools = {};
		if (items) {
			$.each(items, function() {
				selectedTools[this.id] = this;
			});
		}
		$.get('toolconfiguration/get-all-usable').done(function(data) {
			dialog.find('#toolGrid').grid({
				identity : 'id',
				columns : [{
					title : '工具名',
					name : 'name',
					width : 250
				}, {
					title : '工具类型',
					name : 'toolType',
					width : 250
				}, {
					title : '工具地址',
					name : 'serviceUrl',
					width : 'auto'
				}],
				isShowPages : false,
				isUserLocalData : true,
				localData : data
			}).on({
				'selectedRow' : function(e, data) {
					var item = data.item;
					if (data.checked) {
						for (id in selectedTools) {
							if (item.id != id && item.toolType == selectedTools[id].toolType) {
								$('body').message({
									type : 'warning',
									content : '每种工具只能选择一个!'
								});
								$(this).find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + item.id + '"]').removeClass('checked').closest('tr').removeClass('success');
								return;
							}
						}
						if (item.toolType == 'JENKINS') {
							initJenkinsConfig();
						}
						selectedTools[item.id] = item;
					} else {
						delete selectedTools[item.id];
					}
				},
				'complate' : function() {
					var $this = $(this);
					for (id in selectedTools) {
						$this.find('.grid-table-body').find('[data-role="indexCheckbox"][data-value="' + id + '"]').click();
					}
				}
			});
		});
		dialog.find('#save').on('click', function() {
			var tools = [];
			for (id in selectedTools) {
				tools.push(selectedTools[id])
			}
			initToolGrid(tools);
			dialog.modal('hide');
		});
	}
	var initJenkinsConfig = function() {
		$.get('pages/cis/jenkins-config.html').done(function(data) {
			var dialog = $(data);
			var scmType = dialog.find('#scmType');
			var repositoryUrl = dialog.find('#repositoryUrl');
			$.get('toolconfiguration/get-scm-type').done(function(data) {
				var contents = [];
				for (prop in data) {
					contents.push({
						title : prop,
						value : prop
					});
				}
				scmType.select({
					title : '请选择',
					contents : contents
				});
				if (!$.isEmptyObject(jenkinsConfig)) {
					scmType.setValue(jenkinsConfig.scmType);
					repositoryUrl.val(jenkinsConfig.repositoryUrl);
				}
			});
			dialog.modal({
				keyboard : false,
				backdrop : false
			}).on({
				'hidden.bs.modal' : function() {
					$(this).remove();
				}
			});
			dialog.find('#save').on('click', function() {
				if (!Validation.notNull(dialog, scmType, scmType.getValue(), '请选择工具')) {
					return false;
				}
				if (!Validation.notNull(dialog, repositoryUrl, repositoryUrl.val(), '请输入地址')) {
					return false;
				}
				jenkinsConfig = {
					scmType : scmType.getValue(),
					repositoryUrl : repositoryUrl.val()
				}
				dialog.modal('hide');
			});
		});
	}

	projectAdd.find('#complateBtn').on('click', function() {
		projectDto.userCas = projectAdd.find('#isUseCas').hasClass('checked');
		projectDto.scmConfig = jenkinsConfig;
		var developers = projectAdd.find('#developerGrid').getGrid().getAllItems();
		var tools = projectAdd.find('#toolsGrid').getGrid().getAllItems();
		var projectForCis = {};
		if (projectDto.projectForCis) {
			projectForCis.name = projectDto.projectForCis.name;
		}
		projectDto.projectForCis = projectForCis;
		var projectDeveloperDtos = [];
		$.each(developers, function() {
			var roleIds = [];
			$.each(this.roles, function(){
				roleIds.push(this.id);
			});
			projectDeveloperDtos.push({
				developerId : this.id,
				roleIds: roleIds
			});
		});
		projectDto.projectDeveloperDtos = projectDeveloperDtos;
		var projectTools = [];
		$.each(tools, function() {
			projectTools.push({
				id : this.id,
				toolType : this.toolType
			});
		});
		projectDto.toolConfigurationDtos = projectTools;
		var securitySystem = projectAdd.find('#securitySystem');
		var cacheTypeValue = projectAdd.find('#cacheTypeValue').val();
		var monitorSystem = projectAdd.find('#monitorSystem');
		var monitorTypeValue = projectAdd.find('#monitorTypeValue').val();
		var generquerySystem = projectAdd.find('#generquerySystem');
		var organisationSystem = projectAdd.find('#organisationSystem');
		var logSystem = projectAdd.find('#logSystem');
		var system = {};
		if (securitySystem.hasClass('checked')) {
			system.security = {};
			system.security.cacheType = cacheTypeValue;
		}
		if (monitorSystem.hasClass('checked')) {
			system.monitor = {};
			system.monitor.installType = monitorTypeValue;
		}
		if (generquerySystem.hasClass('checked')) {
			system.generalQuery = {};
		}
		if (organisationSystem.hasClass('checked')) {
			system.organization = {};
		}
		if (logSystem.hasClass('checked')) {
			system.businessLog = {};
		}
		$.each(projectDto.projectForCreate.module, function(index) {
			delete this.security;
			delete this.basePackagePath;
			delete this.businessLog;
			delete this.generalQuery;
			delete this.organization;
			delete this.monitor;
			if (this.moduleType == 'war') {
				for (prop in system) {
					this[prop] = system[prop];
				}
			}
		});
		delete projectDto.projectForCreate.path;
		delete projectDto.projectForCreate.scanPackages;
		delete projectDto.projectForCreate.packageName;
		delete projectDto.projectForCreate.groupPackage;
		$.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			'type' : "Post",
			'url' : 'project/create',
			'data' : JSON.stringify(projectDto),
			'dataType' : 'json'
		}).done(function(result) {
			if (result.result) {
				$('.content').message({
					type : 'success',
					content : '创建成功'
				});
				$('#projectList').click();
			} else {
				$('.content').message({
					type : 'error',
					content : '创建失败'
				});
			}
		});
	});

});

var deleteDeveloper = function(id) {
	$('.project-add #developerGrid').getGrid().removeRows([id]);
}
var deleteTool = function(id) {
	$('.project-add #toolsGrid').getGrid().removeRows([id]);
}
