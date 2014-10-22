<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="generalQueryGrid"></div>
<script type="text/javascript" src="<c:url value='/js/gqc/generalQuery.js' />"></script>

<script>
    $(function(){
       var cols = [
            {title:'查询器名称', name:'queryName', width: '150px'},
            { title:'描述', name:'description' , width: '150px'},
            { title:'数据源', name:'dataSourceId', width: '150px', render: function(item, name, index){return item.dataSource.dataSourceId;}},
            { title:'查询表', name:'tableName' , width: '250px'},
            { title:'预览', name:'preview' , width: 'auto', render: function(item, name, index){
                return '<a href="#" onclick="generalQuery().preview('+'\''+item.id+'\''+', '+'\''+item.dataSource.id+'\''+')"><i class="glyphicon glyphicon-eye-open"></i>&nbsp;预览</a>';
            }}
        ];
        var buttons = [
            {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>', action: 'add'},
            {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
            {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;刪除</button>', action: 'delete'}
        ];
        $('#generalQueryGrid').grid({
             identity: 'id',
             columns: cols,
             buttons: buttons,
             querys: [{title: '查询器名称', value: 'queryName'}],
             url: contextPath + '/generalquery/pageJson.koala'
        }).on({
                    'add': function(){
                        generalQuery().add( $(this));
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
                        generalQuery().modify(indexs[0], $this);
                    },
                    'delete': function(event, data){
                        var indexs = data.data;
                        var $this = $(this)
                        if(indexs.length == 0){
                            $this.message({
                                type: 'warning',
                                content: '请选择要删除的记录'
                            })
                            return;
                        }
                        $this.confirm({
                            content: '确定要删除所选记录吗?',
                            callBack: function(){ generalQuery().del(indexs.join(','), $this);}
                        });
                    }
        })
    })
</script>
