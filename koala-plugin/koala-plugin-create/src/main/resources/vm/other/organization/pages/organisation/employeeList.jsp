<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
         { title:'岗位', name:'postName', width: '180px'},
         { title:'兼职岗位', name:'additionalPostNames', width: '180px'},
         { title:'查看', name:'operate', width: '120px', 
        	 render: function(item, name, index){
             	return '<a href="#" onclick="employee().showDetail('+item.id+', \''+item.name+'\')"><span class="glyphicon glyphicon glyphicon-eye-open"></span>&nbsp;详细</a>';
        	 }
         }
     ];
     var buttons = [
         {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;雇佣</button>', action: 'add'},
         {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改基本信息</button>', action: 'modify'},
         {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;解雇</button>', action: 'delete'}     ];
     $('#employeegrid').grid({
          identity: 'id',
          columns: cols,
          buttons: buttons,
          querys: [{title: '姓名', value: 'name'}, {title: '员工编号', value: 'sn'}],
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
                 }
     })
 })
</script>
