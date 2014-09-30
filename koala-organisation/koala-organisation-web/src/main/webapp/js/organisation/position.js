var position = function(){
	var baseUrl =  contextPath + '/post/';
	var dialog = null;    			//对话框
	var positionSn = null;
	var positionName = null;   			//职务名称
	var positionDepartment = null;    			//部门
	var departmentTree = null;        //部门树
	var departmentInput = null;
	var positionJob = null;    			//职务
	var positionDescription = null;    	//职务描述
	var organizationPrincipals = null;
	var dataGrid = null; 			//Grid对象
	var departmentId = null;
	var departmentName = null;
	var positionDto = null;
	/*
	 *新增 
	 */
	var add = function(grid){
		dataGrid = grid;
		$.get( contextPath + '/pages/organisation/position-template.jsp').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(id, grid){
		dataGrid = grid;
		$.get( contextPath + '/pages/organisation/position-template.jsp').done(function(data){
			init(data, id);
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
		    'url': baseUrl + 'terminate-posts.koala',
		    'data': JSON.stringify(objects),
		    'dataType': 'json'
		 }).done(function(data){
			if(data.success){
				dataGrid.message({
					type: 'success',
					content: '撤销成功'
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
		dialog.find('.modal-header').find('.modal-title').html(id ? '修改岗位信息':'创建岗位');
		positionSn = dialog.find('#positionSn');
		positionName = dialog.find('#positionName');
		positionDepartment = dialog.find('#positionDepartment');
		positionDescription = dialog.find('#positionDescription');
		departmentInput = positionDepartment.find('input');
		positionJob = dialog.find('#positionJob');
		organizationPrincipals = dialog.find('[name="organizationPrincipal"]');
		organizationPrincipals.on('click', function(){
			organizationPrincipals.parent().removeClass('checked');
            $(this).parent().addClass('checked');
		});
		loadJobList(id);
		positionDepartment.find('[data-toggle="dropdown"]').on('click', function(){
			selectDepartments();
		});
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
	/**
	 * 部门选择
	 */
	var selectDepartments = function(){
		$.get( contextPath + '/pages/organisation/select-department-template.jsp').done(function(data){
			var departmentTreeDialog = $(data);
            departmentTreeDialog.find('.modal-body').css({height:'325px'});
			departmentTree = departmentTreeDialog.find('.tree');
            loadDepartmentTree();
			departmentTreeDialog.find('#confirm').on('click',function(){
				departmentTreeDialog.modal('hide');
				positionDepartment.find('input').val(departmentId);
				positionDepartment.find('[data-toggle="item"]').html(departmentName);
				positionDepartment.trigger('keydown');
			}).end().modal({
				backdrop: false,
				keyboard: false
			}).on({
				'hidden.bs.modal': function(){
					$(this).remove();
				}
			});
		});
	};
	/**
	 * 加载部门树
	 */
	var loadDepartmentTree = function(){
		departmentTree.parent().loader({
			opacity: 0
		});
        $.get(contextPath  + '/organization/org-tree.koala').done(function(data){
           departmentTree.parent().loader('hide');
           var zNodes = new Array();
            $.each(data, function(){
                var zNode = {};
                if(this.organizationType == 'company'){
                    zNode.type = 'parent';
                }else{
                    zNode.icon = 'glyphicon glyphicon-list-alt'
                }
                this.title = this.name;
                zNode.menu = this;
                if(this.children && this.children.length > 0){
                    zNode.children = getChildrenData(new Array(), this.children);
                }
                zNodes.push(zNode);
            });
            var dataSourceTree = {
                data: zNodes,
                delay: 400
            };
            departmentTree.tree({
                dataSource: dataSourceTree,
                loadingHTML: '<div class="static-loader">Loading...</div>',
                multiSelect: false,
                cacheItems: true
            }).on({
                'selectParent': function(event, data){
                    var data = data.data;
                    departmentId = data.id;
                    departmentName = data.name;
                },
                'selectChildren': function(event, data){
                    departmentId = data.id;
                    departmentName = data.name;
                }
            });
        });
	};
    var getChildrenData = function(nodes, items){
        $.each(items, function(){
            var zNode = {};
            if(this.organizationType == 'company'){
                zNode.type = 'parent';
            }else{
                zNode.icon = 'glyphicon glyphicon-list-alt'
            }
            this.title = this.name;
            zNode.menu = this;
            if(this.children && this.children.length > 0){
                zNode.children = getChildrenData(new Array(), this.children);
            }
            nodes.push(zNode);
        });
        return nodes;
    };
	/**
	 *   加载职务列表
	 * @param id
	 */
	var loadJobList = function(id){
		$.get( contextPath + '/job/query-all.koala').done(function(data){
			var items = data;
			var contents = new Array();
			for(var i= 0, j=items.length; i<j; i++){
				var item = items[i];
				contents.push({value: item.id, title: item.name});
			}
			positionJob.select({
				title: '选择职务',
				contents: contents
			});
			if(id){
				setData(id);
			}
		}).fail(function(data){
				dialog.find('.modal-content').message({
					type: 'error',
					content: '获取职位信息失败'
				});
			});
	};
	/*
	 *设置值
	 */
	var setData = function(id){
		$.get(baseUrl+'get/'+id+'.koala')
			.done(function(result){
				positionDto = result;
				positionSn.val(positionDto.sn);
				departmentInput.val(positionDto.organizationId);
				positionName.val(positionDto.name);
				positionDepartment.find('[data-toggle="item"]').html(positionDto.organizationName);
				positionJob.setValue(positionDto.jobId) ;
				if(positionDto.organizationPrincipal){
					organizationPrincipals.each(function(){
						var $this = $(this);
						$this.val() == 'true' && $this.click();
					});
				}
				positionDescription.val(positionDto.description);
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
		var url = baseUrl + 'create.koala?organizationId='+departmentInput.val();
		if(id){
			url =  baseUrl + 'update.koala?organizationId='+departmentInput.val();
		}
		$.post(url, getAllData(id)).done(function(data){
			if(data.success){
				dialog.trigger('complete');
			}else {
				dialog.find('.modal-content').message({
					type: 'error',
					content: data.errorMessage
				});
				refreshToken(dialog.find('input[name="koala.token"]'));
			}
			dialog.find('#save').removeAttr('disabled');
		});
	};
	/*
	*获取表单数据
	 */
	var getAllData = function(id){
		if(id){
			delete positionDto.organizationName;
			delete positionDto.jobName;
			delete positionDto.employeeCount;
			delete positionDto.jobId;
			delete positionDto.organizationId;
			positionDto.sn = positionSn.val();
			positionDto.name = positionName.val();
			positionDto.jobId = positionJob.getValue();
			positionDto.description = positionDescription.val();
			organizationPrincipals.each(function(){
				if(this.checked){
					positionDto.organizationPrincipal = $(this).val();
				}
			});
			positionDto['koala.token'] = dialog.find('input[name="koala.token"]').val();
			return positionDto;
		}else{
			var data = {};
			data.name = positionName.val();
			data.jobId = positionJob.getValue();
			data.description = positionDescription.val();
			data.sn = positionSn.val();
			organizationPrincipals.each(function(){
				if(this.checked){
					data.organizationPrincipal = $(this).val();
				}
			});
			data['koala.token'] = dialog.find('input[name="koala.token"]').val();
			return data;
		}
	};
	/**
	 * 表单验证 通过返回true  失败返回false
	 */
	var validate = function(){
		if(!Validation.notNull(dialog, positionSn , positionSn.val(), '请填写岗位编号!')){
			return false;
		}
		if(!Validation.serialNmuber(dialog, positionSn, positionSn.val(), '请填写正确的岗位编号！')){
			return false;
		}
		if(!Validation.notNull(dialog, positionName , positionName.val(), '请填写岗位名称!')){
			return false;
		}
		if(!Validation.notNull(dialog, positionDepartment, departmentInput.val(), '请选择机构!')){
			return false;
		}
		if(!Validation.notNull(dialog, positionJob, positionJob.getValue(), '请选择职务!')){
			return false;
		}
		return true;
	};
	return {
		add: add,
		modify: modify,
		del: remove
	};
};