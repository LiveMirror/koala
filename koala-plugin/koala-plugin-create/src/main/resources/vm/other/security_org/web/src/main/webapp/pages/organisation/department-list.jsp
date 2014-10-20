<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>


<style>
    .department-detail .row > .form-group{
        margin-left: 0;
        margin-right: 0;
        border: 1px solid #DDDDDD;
    }
    .department-detail .control-label {
        text-align: right;
    }
    .department-detail .base-info {
        border-width: 1px 0 0 1px;
        border-collapse: separate;
        margin-bottom: 10px;
    }
    .department-detail .grid-table-body{
        height: 210px;
    }
     .base-info {
     	border-collapse: separate;
     }
    .base-info td {
    	border-width: 0 1px 1px 0!important;
    	border-color: transparent #DDDDDD #DDDDDD transparent!important;
    	border-style: solid
    }
    .department-detail .buttons {
     	margin-top: 38%;
    }
</style>
<div class="department-detail" id="departmentDetail">
    <ul class="u-tree tree" id="departmentTree"  oncontextmenu="return false"></ul>
    <div class="right-content">
        <table class="table table-bordered table-hover base-info">
            <tr>
                <td>
                    <label class="col-lg-5 control-label">编号:</label>
                    <div class="col-lg-7">
                        <span data-role="number"></span>
                        <input type="hidden" data-role="id">
                        <input type="hidden" data-role="organizationType">
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="col-lg-5 control-label">名称:</label>
                    <div class="col-lg-7">
                        <span data-role="name"></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="col-lg-5 control-label">机构负责人:</label>
                    <div class="col-lg-7">
                        <span data-role="principalName"></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="col-lg-5 control-label">描述:</label>
                    <div class="col-lg-7">
                        <span data-role="description"></span>
                    </div>
                </td>
            </tr>
        </table>
       
        <div class="btn-group buttons pull-right">
           	<ks:hasSecurityResource identifier="organizationManagerAddCompany"><button id="addCompany"type="button" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span>&nbsp;创建分公司</button></ks:hasSecurityResource>
            <ks:hasSecurityResource identifier="organizationManagerAddDepartment"><button id="addDepartment"type="button" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span>&nbsp;创建下级部门</button></ks:hasSecurityResource>
            <ks:hasSecurityResource identifier="organizationManagerEmployeeListBtn"><button id="employeeListBtn" class="btn btn-info" type="button"><span class="glyphicon glyphicon-user"></span>&nbsp;员工列表</button></ks:hasSecurityResource>
           	<ks:hasSecurityResource identifier="organizationManagerUpdateDepartment"><button id="updateDepartment" type="button" class="btn btn-success"><span class="glyphicon glyphicon-wrench"></span>&nbsp;修改</button></ks:hasSecurityResource>
            <ks:hasSecurityResource identifier="organizationManagerUpdateCompany"><button id="updateCompany" type="button" class="btn btn-success"><span class="glyphicon glyphicon-wrench"></span>&nbsp;修改</button></ks:hasSecurityResource>
            <ks:hasSecurityResource identifier="organizationManagerDelete"><button id="delete" class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;撤销</button></ks:hasSecurityResource>
        </div>
    </div>
    <div style="clear:both;"></div>
</div>
<script type="text/javascript" src="<c:url value='/js/organisation/department.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/organisation/select-employee.js' />"></script>

<script>
    $(function(){
    	department().getTree();
        $('#employeeListBtn').on('click', function(){
            department().showEmployeeList($('.right-content').find('[data-role="id"]').val(), $('#department-employee-list'));
        });
       $('#addDepartment').on('click', function(){
           department().addDepartment($('.right-content').find('[data-role="id"]').val(),
                   $('.right-content').find('[data-role="organizationType"]').val());
        });
        $('#addCompany').on('click', function(){
            department().addCompany($('.right-content').find('[data-role="id"]').val());
        });
        $('#updateDepartment').on('click', function(){
            var id = $('.right-content').find('[data-role="id"]').val();
            department().updateDepartment(id, $('#departmentTree').find('#'+id))
        });
        $('#updateCompany').on('click', function(){
            var id = $('.right-content').find('[data-role="id"]').val();
            department().updateCompany(id, $('#departmentTree').find('#'+id))
        });
       $('#delete').on('click', function(event, data){
            $(this).confirm({
                content: '确定要撤销该机构吗?',
                callBack: function(){
                	var id = $('.right-content').find('[data-role="id"]').val();
                	$.get( contextPath + '/organization/get.koala?id='+id).done(function(data){
            			var org = data;
                        var type = org.organizationType;
            			delete org.children;
                        delete org.organizationType;
                        var $element = $('#departmentTree').find('#'+org.id);
                        if($element.hasClass('tree-folder')){
                        	$element = $element.find('.tree-folder-header');
                        }
                        department().del(org, type, $element);
                	})
                }
            })
        })
    })
</script>
