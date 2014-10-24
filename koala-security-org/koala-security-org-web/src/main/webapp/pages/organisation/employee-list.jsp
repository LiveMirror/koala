<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<form id="employeeSearchForm" target="_self" class="form-horizontal searchCondition">
	<div id="employeeQueryDiv" hidden="true">
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		  <td>
	          <div class="form-group">
	              <label class="control-label" style="width:100px;float:left;">人员编号:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="sn" class="form-control" type="text" style="width:180px;"/>
	              </div>
	
	              <label class="control-label" style="width:100px;float:left;">人员姓名:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="name" class="form-control" type="text" style="width:180px;"/>
	              </div>
	          </div>
	      </td>
	      <td style="vertical-align: bottom;"><button id="employeeSearchBtn" type="button" style="position:relative; margin-left:35px; top: -15px" class="btn btn-success"><span class="glyphicon glyphicon-search"></span>&nbsp;</button></td>
	  </tr>
	</table>	
	</div>
</form>


<div id="employeegrid"></div>
<script type="text/javascript" src="<c:url value='/js/organisation/employee.js' />"></script>

<script>
$(function(){
    var cols = [
         { title:'姓名', name:'name' , width: '100px'},
         { title:'员工编号', name:'sn', width: '100px'},
         { title:'性别', name:'gender', width: '60px',
             render: function(item, name, index){
                 if(item[name] == 'FEMALE'){
                     return '女';
                 }else{
                     return '男';
                 }
             }
         },
         { title:'入职日期', name:'entryDate', width: '100px'},
         { title:'所属机构', name:'organizationName', width: '210px'},
         { title:'岗位', name:'postName', width: '130px'},
         { title:'兼职岗位', name:'additionalPostNames', width: '180px'},
         { title:'查看', name:'operate', width: '50px', 
        	 render: function(item, name, index){
             	return '<a href="#" onclick="employee().showDetail('+item.id+', \''+item.name+'\')"><span class="glyphicon glyphicon glyphicon-eye-open"></span>&nbsp;详细</a>';
        	 }
         }
     ];
     var buttons = [
         {content: '<ks:hasSecurityResource identifier="employeeManagerCreate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;雇佣</button></ks:hasSecurityResource>', action: 'add'},
         {content: '<ks:hasSecurityResource identifier="employeeManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改基本信息</button></ks:hasSecurityResource>', action: 'modify'},
         {content: '<ks:hasSecurityResource identifier="employeeManagerDelete"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;解雇</button></ks:hasSecurityResource>', action: 'delete'},
         {content: '<ks:hasSecurityResource identifier="employeeManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索 <span class="caret"></span></button></ks:hasSecurityResource>', action: 'search'}
         ];
     $('#employeegrid').grid({
          identity: 'id',
          columns: cols,
          buttons: buttons,
          url: contextPath + '/employee/pagingquery.koala'
     }).on({
                 'add': function(){
                     employee().add( $(this));
                 },
                 'modify': function(event, data){
                     var indexs = data.data;
                     var $this = $(this);
                     if(indexs.length == 0){
                         $this.message({
                             type: 'warning',
                             content: '请选择一条记录进行修改'
                         });
                         return;
                     }
                     if(indexs.length > 1){
                         $this.message({
                             type: 'warning',
                             content: '只能选择一条记录进行修改'
                         });
                         return;
                     }
                     employee().modify(indexs[0], $this);
                 },
                 'delete': function(event, data){
                     var indexs = data.data;
                     var $this = $(this)
                     if(indexs.length == 0){
                         $this.message({
                             type: 'warning',
                             content: '请选择要操作的记录'
                         });
                         return;
                     }
                     $this.confirm({
                         content: '确定要删除所选记录吗?',
                         callBack: function(){ employee().del(data.item, $this);}
                     });
                 },
				'search' : function() {						
					$("#employeeQueryDiv").slideToggle("slow");						 
				}
     })
     
     var form = $("#employeeSearchForm");
        form.find('#employeeSearchBtn').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
            $("#employeegrid").getGrid().search(params);
        });
 })
</script>
