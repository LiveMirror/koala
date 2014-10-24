var employee = function(){
    var baseUrl =  contextPath + '/employee/';
	var dialog = null;   //对话框
	var employeeId = null;
	var personId = null;
	var personVersion = null;
	var createDate = null;
	var terminateDate = null;
	var sn = null;   //编号
	var name = null;    //姓名
	var gender = null;    	//性别
	var idNumber = null;//身份证号码
	var selectDepartment = null;//
	var departmentInput = null;
	var departmentId = null;//
	var departmentName = null;
	var post = null; //岗位
	var email = null; // email
	var mobilePhone = null; //手机号码
	var familyPhone = null; //家庭电话
	var entryDate = null; //入职时间
	var dataGrid = null; 			//Grid对象
	var version = null; 
	var operateType = null;//操作类型
	var departmentTree = null;
	$.ajaxSetup({cache:false});
	/*
	 *新增 
	 */
	var add = function(grid){
		dataGrid = grid;
		operateType = 'add';
		$.get( contextPath + '/pages/organisation/employee-template.jsp').done(function(data){
			init(data);
		});
	};
	/*
	 * 修改
	 */
	var modify = function(id, grid){
		dataGrid = grid;
		operateType = 'update';
		$.get( contextPath + '/pages/organisation/employee-update.jsp').done(function(data){
			init(data, id);
			setData(id);
		});
	};
	/*
	 删除方法
	 */
	var remove = function(objects, grid){
		dataGrid = grid;
		
		$.ajax({
		    headers: { 
		        'Accept': 'application/json',
		        'Content-Type': 'application/json' 
		    },
		    'type': "Post",
		    'url': baseUrl + 'terminate-employees.koala',
		    'data': JSON.stringify(objects),
		    'dataType': 'json'
		 }).done(function(data){
			if(data.success){
				dataGrid.message({
					type: 'success',
					content: '解雇成功'
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
				content: '解雇失败'
			});
		});
	};
	/**
	 * 初始化
	 */
	var init = function(data, id){
		dialog = $(data);
		dialog.find('.modal-header').find('.modal-title').html(id ? '修改员工信息':'创建员工信息');
		sn = dialog.find('#employee-sn');
		name = dialog.find('#employee-name');
		gender = dialog.find('#employee-gender');
		idNumber = dialog.find('#employee-idNumber');
		email = dialog.find('#employee-email');
		mobilePhone = dialog.find('#employee-mobilePhone');
		familyPhone = dialog.find('#employee-familyPhone');
		entryDate = dialog.find('#employee-entryDate');
		fillGenders();
		initEntryDate();
		if (operateType == "add") {
			selectDepartment = dialog.find('#select-department');
			departmentInput = selectDepartment.find('input');
			selectDepartment.find('[data-toggle="dropdown"]').on('click', function(){
				selectDepartments();
			});
			post = dialog.find('#employee-post');
		}
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
				$('body').message({
					type: 'success',
					content: '保存成功'
				});
				$(this).modal('hide');
				dataGrid.grid('refresh');
			}
		});
	};
	/**
	 * 性别选择
	 */
	var  fillGenders = function(){
		$.get( contextPath + '/employee/genders.koala').done(function(data){
			var items = data;
			var contents = new Array();
			for(var prop in items){
				contents.push({value: prop, title: items[prop]});
			}
			gender.select({
				title: '选择性别',
				contents: contents
			});
		}).fail(function(data){
			dialog.find('.modal-content').message({
				type: 'error',
				content: '获取性别信息失败'
			});
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
				departmentInput.val(departmentId);
				selectDepartment.find('[data-toggle="item"]').text(departmentName);
				departmentTreeDialog.modal('hide');
				selectDepartment.trigger('keydown');
				fillPosts(departmentId);
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
	 * 职位选择
	 */
	var  fillPosts = function(organizationId){
		$.get( contextPath + '/post/query-post-by-org.koala?organizationId='+organizationId).done(function(data){
			 var items = data;
			 var contents = new Array();
			 for(var i= 0, j=items.length; i<j; i++){
				 var item = items[i];
				 contents.push({value: item.id, title: item.name});
			 }
			 post.data('koala.select', null);
			 post.select({
				title: '选择岗位',
				contents: contents
			});
		}).fail(function(data){
			dialog.find('.modal-content').message({
				type: 'error',
				content: '获取职位信息失败'
			});
		});
	};
	/**
	 * 日期
	 */
	var initEntryDate = function(){
		entryDate.datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            autoclose: true,
            todayBtn: true,
            minView: 2,
            pickerPosition: 'bottom-left'
        }).datetimepicker('setDate', new Date());
	};
	
	/*
	 *设置值
	 */
	var setData = function(id){
		$.get(baseUrl+'get/'+id+'.koala')
			.done(function(result){
				var data = result;
				employeeId = data.id;
				personId = data.personId;
				personVersion = data.personVersion;
				version = data.version;
				createDate = data.createDate;
				terminateDate = data.terminateDate;
				name.val(data.name);
				sn.val(data.sn);
				gender.setValue(data.gender);
				idNumber.val(data.idNumber);
				email.val(data.email);
				mobilePhone.val(data.mobilePhone);
				familyPhone.val(data.familyPhone);
				entryDate.find('input').val(data.entryDate);
//				entryDate.datetimepicker('setValue',data.entryDate);
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
		var url = '';
		if(id){
			url =  baseUrl + 'update.koala';
		}else{
			url = baseUrl + 'create.koala?postId='+post.getValue();
		}
		$.post(url, getAllData(id)).done(function(data){
			if(data.success){
				dialog.trigger('complete');
			}else{
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
	var getAllData = function(){
		var data = {};
		data.gender = gender.getValue();
		
		if (checkNotNull(idNumber.val())) {
			data.idNumber = $.trim(idNumber.val());
		}
		if (checkNotNull(mobilePhone.val())) {
			data.mobilePhone = $.trim(mobilePhone.val());
		}
		if (checkNotNull(familyPhone.val())) {
			data.familyPhone = $.trim(familyPhone.val());
		}
		if (checkNotNull(email.val())) {
			data.email = $.trim(email.val());
		}
		
		data.name = name.val();
		data.sn = sn.val();
		data.entryDate = entryDate.find('input').val();
		
		if (employeeId != null) {
			data.id = employeeId;
		}
		if (personId != null) {
			data.personId = personId;
		}
		if (personVersion != null) {
			data.personVersion = personVersion;
		}
		if (version != null) {
			data.version = version;
		}
		if (createDate != null) {
			data.createDate = createDate;
		}
		if (terminateDate != null) {
			data.terminateDate = terminateDate;
		}
		data['koala.token'] = dialog.find('input[name="koala.token"]').val();
		return data;
	};
	/**
	 * 表单验证 通过返回true  失败返回false
	 */
	var validate = function(){
		if(!Validation.notNull(dialog, sn, sn.val(), '请输入员工编号')){
			return false;
		}
		if(!Validation.serialNmuber(dialog, sn, sn.val(), '请输入正确的员工编号')){
			return false;
		}
		if(!Validation.notNull(dialog, name, name.val(), '请输入员工姓名')){
			return false;
		}
		if(!Validation.humanName(dialog, name, name.val(), '请输入正确的姓名')){
			return false;
		}
		if(!Validation.notNull(dialog, gender, gender.getValue(), '请选择员工性别')){
			return false;
		}
		if(operateType == 'add') {
			if(!Validation.notNull(dialog, selectDepartment, departmentInput.val(), '请选择所属机构')){
				return false;
			}
			if(!Validation.notNull(dialog, post, post.getValue(), '请选择任职岗位')){
				return false;
			}
		}
		if (checkNotNull(email.val())) {
			if (!Validation.email(dialog, email, email.val(), '请输入正确的email')) {
				return false;
			}
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
	
	/**
	 * 检查变量是否不为空  true:不空   false:空
	 */
	var checkNotNull = function(item) {
		//不能为空和空格
		if(item==null || item=="" || item.replace(/(^\s*)|(\s*$)/g, "")=="" ){
			return false;
		}else{
			return true;
		}
	};
	/**
	 * 显示详细信息
	 */
	var showDetail = function(id, employeeName){
		openTab('/pages/organisation/employee-detail.jsp', employeeName, 'employeeDetail', id);
	};
	return {
		add: add,
		modify: modify,
		del: remove,
		showDetail: showDetail
	};
};
