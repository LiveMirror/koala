<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="positionGrid"></div>
<script type="text/javascript" src="<c:url value='/js/organisation/position.js' />"></script>

<script>
    $(function(){
       var cols = [
            { title:'岗位编号', name:'sn' , width: '100px'},
            { title:'岗位名称', name:'name' , width: '150px'},
            { title:'部门', name:'organizationName', width: '150px'},
            { title:'职务', name:'jobName', width: '150px'},
            { title:'是否机构负责岗位', name:'organizationPrincipal', width: '150px', 
	           	 render: function(item, name, index){
	              	return item[name] ? '是':'否';
	         	 }
	        },
            { title:'任职员工人数', name:'employeeCount' , width: '150px'},
            { title:'描述', name:'description' , width: 'auto'}
        ];
        var buttons = [
            {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;创建</button>', action: 'add'},
            {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
            {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;撤销</button>', action: 'delete'}
        ];
        $('#positionGrid').grid({
             identity: 'id',
             columns: cols,
             buttons: buttons,
             querys: [{title: '岗位名称', value: 'name'}],
             url:  contextPath + '/post/pagingquery.koala'
        }).on({
                    'add': function(){
                        position().add( $(this));
                    },
                    'modify': function(event, data){
                        var indexs = data.data;
                        var $this = $(this);
                        if(indexs.length == 0){
                            $this.message({
                                type: 'warning',
                                content: '请选择一条记录进行修改'
                            })
                            return;
                        }
                        if(indexs.length > 1){
                            $this.message({
                                type: 'warning',
                                content: '只能选择一条记录进行修改'
                            })
                            return;
                        }
                        position().modify(indexs[0], $this);
                    },
                    'delete': function(event, data){
                        var indexs = data.data;
                        var $this = $(this)
                        if(indexs.length == 0){
                            $this.message({
                                type: 'warning',
                                content: '请选择要撤销的岗位'
                            })
                            return;
                        }
                        $this.confirm({
                            content: '确定要撤销所选岗位吗?',
                            callBack: function(){ position().del(data.item, $this);}
                        });
                    }
        })
    })
</script>