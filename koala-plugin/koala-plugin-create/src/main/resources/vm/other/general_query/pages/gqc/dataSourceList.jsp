<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="dataSourceGrid"></div>
<script type="text/javascript" src="<c:url value='/js/gqc/dataSource.js' />"></script>
<script>
$(function(){
    var cols = [
        {title:'数据源ID', name:'dataSourceId', width: '180'},
        { title:'类型', name:'dataSourceType' , width: '120'},
        { title:'JdbcDriver', name:'jdbcDriver' , width: '320'},
        { title:'Url', name:'connectUrl' , width: '350'},
        { title:'UserName', name:'username', width: '120'},
        { title:'Password', name:'password' , width: '120'},
        { title:'描述', name:'dataSourceDescription', width: '250'},
        { title:'操作', name:'operate' , width: '120', render: function(item, name, index){
            return '<a href="#" onclick="dataSource().testConnectionById('+item.id+')"><span class="glyphicon glyphicon-transfer"></span>测试连接</a>';
        }}
    ];
    var buttons = [
        {content: '<button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>', action: 'add'},
        {content: '<button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button>', action: 'modify'},
        {content: '<button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;刪除</button>', action: 'delete'}
    ];
    var data =
    $('#dataSourceGrid').grid({
        identity: 'id',
        columns: cols,
        buttons: buttons,
        url: contextPath + '/dataSource/pageJson.koala'
    }).on({
        'add': function(){
            dataSource().add( $(this));
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
            dataSource().modify(indexs[0], $this);
         },
        'delete': function(event, data){
            var indexs = data.data;
            var $this = $(this);
            if(indexs.length == 0){
                $this.message({
                    type: 'warning',
                    content: '请选择要删除的记录'
                })
                return;
            }
            $this.confirm({
                content: '确定要删除所选记录吗?',
                callBack: function(){ dataSource().del(indexs.join(','), $this);}
            });
        }
    });
})
</script>