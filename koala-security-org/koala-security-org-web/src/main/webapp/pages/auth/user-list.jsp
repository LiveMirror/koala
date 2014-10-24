<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<form name="userListForm" id="${formId}" target="_self" class="form-horizontal searchCondition">
<div id="userManagerQueryDivId" class="panel" hidden="true">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
          <div class="form-group">
          <label class="control-label" style="width:100px;float:left;">用户名称:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="name" class="form-control" type="text" style="width:180px;" id="nameID"  dataType="Chinese" require="false" />
        </div>
            <label class="control-label" style="width:100px;float:left;">用户帐号:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="userAccount" class="form-control" type="text" style="width:180px;" id="descID"  />
        </div>
            </div>
            <div class ="form-group">
             <label class="control-label" style="width:100px;float:left;">用户描述:&nbsp;</label>
            <div style="margin-left:15px;float:left;">
            <input name="description" class="form-control" type="text" style="width:180px;"  />
        </div>
           <label class="control-label" style="width:100px;float:left;">用户状态:&nbsp;</label>
           <select  name="disabled" class="form-control"  style = "width:180px;margin-left:15px;float:left;">
          	 <option value="">全部</option>
             <option value="false">激活</option>
             <option value="true">禁用</option>
           </select>
            </div>
            </td>
       <td style="vertical-align: bottom;"><button id="userManagerSearch" type="button" style="position:relative; margin-left:35px; top: -15px" class="btn btn-success glyphicon glyphicon-search"></button></td>
  </tr>
</table>	
</div>
</form>

<div data-role="userGrid">
</div>
	<script type="text/javascript">
    var departmentTree = null;
    var positionDepartment = null;
    var departmentId = null;
    var departmentName = null;

	$(function (){
		PageLoader = {
		    initSearchPanel:function(){},
		    initGridPanel: function(){
		         var self = this;
		         var url = contextPath + '/auth/employeeUser/pagingQuery.koala';
		         var getButtons = function() {
                    return [{
                        content : '<ks:hasSecurityResource identifier="userManagerAdd"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"><span>添加</button></ks:hasSecurityResource>',
                        action : 'add'
                    }, {
                        content : '<ks:hasSecurityResource identifier="userManagerUpdate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-edit"><span>修改</button></ks:hasSecurityResource>',
                        action : 'modify'
                    }, {
                        content : '<ks:hasSecurityResource identifier="userManagerTerminate"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"><span>撤销</button></ks:hasSecurityResource>',
                        action : 'delete'
                    }, {
                        content : '<ks:hasSecurityResource identifier="userManagerGrantRole"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配角色</button></ks:hasSecurityResource>',
                        action : 'grantRoleToUser'
                    }, {
                        content : '<ks:hasSecurityResource identifier="userManagerGrantPermission"><button class="btn btn-info" type="button"><span class="glyphicon glyphicon-th-large"></span>&nbsp;分配权限</button></ks:hasSecurityResource>',
                        action : 'grantPermissionToUser'
                    }, {
                        content : '<ks:hasSecurityResource identifier="userManagerResetPassword"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;重置密码</button></ks:hasSecurityResource>',
                        action : 'resetPassword'
                    },{
                        content : '<ks:hasSecurityResource identifier="userManagerSuspend"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;禁用</button></ks:hasSecurityResource>',
                        action : 'forbidden'
                    },{
                        content : '<ks:hasSecurityResource identifier="userManagerActivate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-wrench"></span>&nbsp;激活</button></ks:hasSecurityResource>',
                        action : 'available'
                    },{
                        content : '<ks:hasSecurityResource identifier="userManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索&nbsp; <span class="caret"></span> </button></ks:hasSecurityResource>',
                        action : 'userManagerQuery'
                    }];
		 		};
		         return $('[data-role="userGrid"]').off().grid({
		                identity:"id",
		                buttons: getButtons(),
		                url:url,
		                columns: [{
							title : "用户名称",
							name : "name",
							width : 100
						}, {
							title : "用户帐号",
							name : "userAccount",
							width : 100
						}, {
                            title : "关联员工",
                            name : "employeeName",
                            width : 100
                        }, {
							title : "创建人",
							name : "createOwner",
							width : 100
						}, {
							title : "创建时间",
							name : "createDate",
							width : 100
						}, {
							title : "最后更改时间",
							name : "lastModifyTime",
							width : 100
						}, {
							title : "用户描述",
							name : "description",
							width : 150
						}, {
							title : "是否有效",
							name : "disabled",
							width : 80,
							render : function(item, name, index) {
								return item[name]?  '<span class="glyphicon glyphicon-remove" style="color:#D9534F;margin-left:15px;"></span>':'<span class="glyphicon glyphicon-ok" style="color:#5CB85C;margin-left:15px;"></span>';
							}  
						}, {
							title : "查看",
							name : "operate",
							width : 200,
							 render: function(item, name, index){
					             	return '<a href="#" onclick="showUserDetail('+item.id+', \''+item.name+'\')"><span class="glyphicon glyphicon glyphicon-eye-open"></span>&nbsp;详细</a>';
					        	 }
						}
		                ]
		         }).on({
		 			'add' : function() {
						userManager().add($(this));
					},
					'modify' : function(event, data) {
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
						userManager().modify(data.item[0], $(this));
					},
					'delete' : function(event, data) {
						var indexs = data.data;
						var $this = $(this);

                        if (indexs.length == 0) {
							$this.message({
								type : 'warning',
								content : '请选择要撤销的记录'
							});
                            return;
						}

						$this.confirm({
							content : '确定要撤销所选记录吗?',
							callBack : function() {
								userManager().deleteUser(data.item, $this);
							}
						});
					},
					'resetPassword' : function(event, data) {
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
					
						userManager().resetPassword(data.item[0], $(this));
					},
					'forbidden' : function(event, data) {
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
						
						userManager().forbidden(data.item[0] , $this);
					},
					'userManagerQuery' : function() {
						$("#userManagerQueryDivId").slideToggle("slow");
						
					},
					'available' : function(event, data) {
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
						userManager().available(data.item[0] , $this);
				    },
				    
				    
				    
                    'grantRoleToUser' : function(event, data) {
                        var indexs = data.data;
                        var $this = $(this);
                        if (indexs.length == 0) {
                            $this.message({
                                type : 'warning',
                                content : '请选择一条记录进行授权角色'
                            });
                            return;
                        }
                        if (indexs.length > 1) {
                            $this.message({
                                type : 'warning',
                                content : '只能选择一条记录进行授权角色'
                            });
                            return;
                        }

                        var userId = data.item[0].id;
                        var userAccount = data.item[0].userAccount;
                        var employeeOrgName = data.item[0].employeeOrgName;
                        var employeeOrgId = data.item[0].employeeOrgId;
                        if(employeeOrgName == null){
                            employeeOrgName = "";
                        }

                        $.get(contextPath + '/pages/auth/user-grantAuthorityToUser.jsp').done(function(data){
                            var dialog  = $(data);

                            dialog.find('.modal-header').find('.modal-title').html('为用户[' + userAccount + ']授权角色');

                                dialog.find('#grantAuthorityToUserButton').on('click',function(){
                                    var grantRolesToUserTableItems = dialog.find('#notGrantAuthoritiesToUserGrid').getGrid().selectedRows();

                                    if(grantRolesToUserTableItems.length == 0){
                                        dialog.find('#notGrantAuthoritiesToUserGrid').message({
                                            type: 'warning',
                                            content: '请选择要需要被授权的角色'
                                        });
                                        return;
                                    }

                                    var data = "actorId="+userId;

                                    if(employeeOrgId != null){
                                        data +="&organizationId=" + employeeOrgId;
                                        data +="&organizationName=" + employeeOrgName;
                                    }

                                    var url = contextPath+'/auth/employeeUser/grantAuthorityToActorInScope.koala';

                                    $.each(grantRolesToUserTableItems,function(index,grantRolesToUserTableItem){
                                        data += ("&authorityIds=" + grantRolesToUserTableItem.id + "&");
                                    });
                                    data = data.substring(0, data.length-1);
                                    $.post(url, data).done(function(data) {
                                        if(data.success){
                                            dialog.find('#grantAuthorityToUserMessage').message({
                                                type: 'success',
                                                content: '为用户授权角色成功'
                                            });
                                            dialog.find('#notGrantAuthoritiesToUserGrid').grid('refresh');
                                            dialog.find('#grantAuthoritiesToUserGrid').grid('refresh');
                                        }
                                    });
                                });

                            dialog.find('#notGrantAuthorityToUserButton').on('click',function(){
                                var notGrantRolesToUserGridItems = dialog.find('#grantAuthoritiesToUserGrid').getGrid().selectedRows();

                                if(notGrantRolesToUserGridItems.length == 0){
                                    dialog.find('#grantAuthoritiesToUserGrid').message({
                                        type: 'warning',
                                        content: '请选择要需要撤销的角色'
                                    });
                                    return;
                                }

                                var data = "userId=" + userId;
                                $.each(notGrantRolesToUserGridItems,function(index,notGrantRolesToUserGridItem){
                                    data += ("&roleIds=" + notGrantRolesToUserGridItem.id + "&");
                                });
                                data = data.substring(0, data.length-1);
                                $.post(contextPath + '/auth/user/terminateAuthorizationByUserInRoles.koala', data).done(function(data) {
                                    if(data.success){
                                        dialog.find('#grantAuthorityToUserMessage').message({
                                            type: 'success',
                                            content: '撤销用户的角色成功！'
                                        });
                                        dialog.find('#notGrantAuthoritiesToUserGrid').grid('refresh');
                                        dialog.find('#grantAuthoritiesToUserGrid').grid('refresh');
                                    }
                                });

                            });

                            dialog.modal({
                                keyboard: false,
                                backdrop: false
                            }).on({
                                'hidden.bs.modal' : function(){
                                    $(this).remove();
                                },
                                'shown.bs.modal' : function(){
                                    var hasGrantRoleColumns = [{
                                        title : "角色名称",
                                        name : "name",
                                        width : 80
                                    }, {
                                        title : "角色描述",
                                        name : "description",
                                        width : 100
                                    }, {
                                       title : "组织机构",
                                       name : "employeeUserOrgName",
                                       width : 100,
                                       render: function(item){
                                           var employeeOrgNameValue = item.employeeUserOrgName;
                                           if(employeeOrgNameValue == null){
                                               employeeOrgNameValue = employeeOrgName;
                                           }
                                           return '<span id="selectemployeeOrgOfRole" onclick="selectemployeeOrg(event,\''+userId+'\',\''+item.id+'\');" style="cursor: pointer;">' + employeeOrgNameValue +'&nbsp;&nbsp;&nbsp;'+ '<span class="glyphicon glyphicon-edit" style="color: green"></span></span>';
                                       }
                                    }];

                                    var notGrantRolecolumns = [{
                                        title : "角色名称",
                                        name : "name",
                                        width : 80
                                    }, {
                                        title : "角色描述",
                                        name : "description",
                                        width : 100
                                    }, {
                                       title : "组织机构",
                                       name : "employeeOrgName",
                                       width : 100,
                                       render: function(){
                                            return '<span>' + employeeOrgName + '</span>';
                                       }
                                    }];

                                    dialog.find('#notGrantAuthoritiesToUserGrid').grid({
                                        identity: 'id',
                                        columns: notGrantRolecolumns,
                                        url: contextPath + '/auth/user/pagingQueryNotGrantRoles.koala?userId='+userId
                                    });

                                    dialog.find('#grantAuthoritiesToUserGrid').grid({
                                        identity: 'id',
                                        columns: hasGrantRoleColumns,
                                        url: contextPath + '/auth/employeeUser/pagingQueryGrantRoleByUserId.koala?userId=' + userId
                                    });
                                }

                            });

                            //兼容IE8 IE9
                            if(window.ActiveXObject){
                                if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                                    dialog.trigger('shown.bs.modal');
                                }
                            }

                        });

                    },
                    'grantPermissionToUser' : function(event, data){

                        var indexs = data.data;
                        var $this = $(this);
                        if (indexs.length == 0) {
                            $this.message({
                                type : 'warning',
                                content : '请选择一条记录进行授权权限'
                            });
                            return;
                        }
                        if (indexs.length > 1) {
                            $this.message({
                                type : 'warning',
                                content : '只能选择一条记录进行授权权限'
                            });
                            return;
                        }

                        var userId = data.item[0].id;
                        var userAccount = data.item[0].userAccount;
                        var employeeOrgName = data.item[0].employeeOrgName;
                        var employeeOrgId = data.item[0].employeeOrgId;

                        if(employeeOrgName == null){
                             employeeOrgName = "";
                        }
                        $.get(contextPath + '/pages/auth/user-grantAuthorityToUser.jsp').done(function(data){
                            var dialog  = $(data);

                            dialog.find('.modal-header').find('.modal-title').html('为用户['+userAccount+']授权权限');
                            dialog.find('.modal-body').find('.left-modal-body').html('已授权权限');
                            dialog.find('.modal-body').find('.right-modal-body').html('未授权权限');

                            dialog.find('#grantAuthorityToUserButton').on('click',function(){
                                var grantPermissionsToUserItems = dialog.find('#notGrantAuthoritiesToUserGrid').getGrid().selectedRows();

                                if(grantPermissionsToUserItems.length == 0){
                                    dialog.find('#notGrantAuthoritiesToUserGrid').message({
                                        type: 'warning',
                                        content: '请选择要需要被授权的权限'
                                    });
                                    return;
                                }

                                var data = "actorId="+userId;
                                if(employeeOrgId != null) {
                                    data +="&organizationName=" + employeeOrgName;
                                    data +="&organizationId=" + employeeOrgId;
                                }
                                var url = contextPath+'/auth/employeeUser/grantAuthorityToActorInScope.koala';

                                $.each(grantPermissionsToUserItems,function(index,grantPermissionsToUserItem){
                                    data += ("&authorityIds=" + grantPermissionsToUserItem.id + "&");
                                });
                                data = data.substring(0, data.length-1);

                                $.post(url, data).done(function(data) {
                                    if(data.success){
                                        dialog.find('#grantAuthorityToUserMessage').message({
                                            type: 'success',
                                            content: '为用户授权权限成功'
                                        });
                                        dialog.find('#notGrantAuthoritiesToUserGrid').grid('refresh');
                                        dialog.find('#grantAuthoritiesToUserGrid').grid('refresh');
                                    }
                                });
                            });

                            dialog.find('#notGrantAuthorityToUserButton').on('click',function(){
                                var notGrantRolesToUserItems = dialog.find('#grantAuthoritiesToUserGrid').getGrid().selectedRows();

                                if(notGrantRolesToUserItems.length == 0){
                                    dialog.find('#grantAuthoritiesToUserGrid').message({
                                        type: 'warning',
                                        content: '请选择要需要撤销的权限'
                                    });
                                    return;
                                }

                                var data = "userId="+userId;
                                $.each(notGrantRolesToUserItems,function(index,notGrantRolesToUserItem){
                                    data += ("&permissionIds=" + notGrantRolesToUserItem.id + "&");
                                });
                                data = data.substring(0, data.length-1);
                                $.post(contextPath + '/auth/user/terminatePermissionsByUser.koala', data).done(function(data) {
                                    if(data.success){
                                        dialog.find('#grantAuthorityToUserMessage').message({
                                            type: 'error',
                                            content: '撤销用户的权限成功！'
                                        });
                                        dialog.find('#notGrantAuthoritiesToUserGrid').grid('refresh');
                                        dialog.find('#grantAuthoritiesToUserGrid').grid('refresh');
                                    }
                                });

                            });

                            dialog.modal({
                                keyboard: false,
                                backdrop: false
                            }).on({
                                'hidden.bs.modal' : function(){
                                    $(this).remove();
                                },
                                'shown.bs.modal' : function(){
                                    var hasGrantPermissionColumns = [{
                                        title : "权限名称",
                                        name : "name",
                                        width : 100
                                    }, {
                                        title : "权限标识",
                                        name : "identifier",
                                        width : 100
                                    }, {
                                        title : "组织机构",
                                        name : "employeeUserOrgName",
                                        width : 150,
                                        render: function(item){
                                            var employeeOrgNameValue = item.employeeUserOrgName;
                                            if(employeeOrgNameValue == null){
                                                employeeOrgNameValue = employeeOrgName;
                                            }
                                            return '<span style="cursor: pointer;" onclick="selectemployeeOrg(event,\''+userId+'\',\''+item.id+'\');">' + employeeOrgNameValue +'&nbsp;&nbsp;&nbsp;'+ '<span class="glyphicon glyphicon-edit" style="color: green"></span></span>';
                                        }
                                    }];

                                    var notGrantPermissioncolumns = [{
                                        title : "权限名称",
                                        name : "name",
                                        width : 100
                                    }, {
                                        title : "权限标识",
                                        name : "identifier",
                                        width : 100
                                    }, {
                                        title : "组织机构",
                                        name : "employeeOrgName",
                                        width : 150,
                                        render: function(){
                                            return '<span>' + employeeOrgName + '</span>';
                                        }
                                    }];

                                    dialog.find('#notGrantAuthoritiesToUserGrid').grid({
                                        identity: 'id',
                                        columns: notGrantPermissioncolumns,
                                        url: contextPath + '/auth/user/pagingQueryNotGrantPermissions.koala?userId='+userId
                                    });

                                    dialog.find('#grantAuthoritiesToUserGrid').grid({
                                        identity: 'id',
                                        columns: hasGrantPermissionColumns,
                                        url: contextPath + '/auth/employeeUser/pagingQueryGrantPermissionByUserId.koala?userId='+userId
                                    });
                                }

                            });

                            //兼容IE8 IE9
                            if(window.ActiveXObject){
                                if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                                    dialog.trigger('shown.bs.modal');
                                }
                            }

                        });
                    }
				});
		    }
		};
		PageLoader.initSearchPanel();
		PageLoader.initGridPanel();

        var form = $('#'+'${formId}');
        form.find('#userManagerSearch').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
            $('[data-role="userGrid"]').getGrid().search(params);
        });

      });

        /**
        * 显示详细信息
        * @param id
        * @param userName
        */
        var showUserDetail = function(id, userName){
              var thiz 	= $(this);
              var  mark 	= thiz.attr('mark');
              mark = openTab('/pages/auth/user-detail.jsp', userName, mark,id);
              if(mark){
                  thiz.attr("mark",mark);
              }
        };
        /**
         * 选择一个组织机构
         */
        var selectemployeeOrg = function (event, actorId, authorityId) {
            // 阻止事件冒泡和捕捉
            event.cancelBubble = true;

            /**
             * 部门选择
             */
            $.get( contextPath + '/pages/organisation/select-department-template.jsp').done(function(data){
                var departmentTreeDialog = $(data);
                positionDepartment = departmentTreeDialog.find('#positionDepartment');
                departmentTreeDialog.find('.modal-body').css({height:'325px'});
                departmentTree = departmentTreeDialog.find('.tree');
                loadDepartmentTree(departmentTree);
                departmentTreeDialog.find('#confirm').on('click',function(){
                    departmentTreeDialog.modal('hide');
                    positionDepartment.find('input').val(departmentId);
                    positionDepartment.find('[data-toggle="item"]').html(departmentName);
                    positionDepartment.trigger('keydown');
                    var data = {};
                    data['actorId'] = actorId;
                    data['authorityIds'] = authorityId;
                    data['organizationId'] = departmentId;
                    data['organizationName'] = departmentName;
                    $.post(contextPath + '/auth/employeeUser/grantRolesToUserInScope.koala', data, function (data) {
                        if(data.success){
                            $('#grantAuthoritiesToUserGrid').grid('refresh');
                        }
                    });

                }).end().modal({
                    backdrop: false,
                    keyboard: false
                }).on({
                    'hidden.bs.modal': function(){
                        $(this).remove();
                    }
                });
            });
        }

        /**
         * 加载部门树
         */
        var loadDepartmentTree = function(departmentTree){
            $.get( contextPath + '/pages/organisation/selectDepartmentTemplate.jsp').done(function(data){
                var departmentTreeDialog = $(data);
                departmentTreeDialog.find('.modal-body').css({height:'325px'});
                departmentTree = departmentTreeDialog.find('.tree');
                loadDepartmentTree(departmentTree);
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
        }

        /**
         * 加载部门树
         */
        var loadDepartmentTree = function(departmentTree){
            departmentTree.parent().loader({
                opacity: 0
            });
            $.get(contextPath + '/organization/org-tree.koala').done(function(data){
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
    </script>
