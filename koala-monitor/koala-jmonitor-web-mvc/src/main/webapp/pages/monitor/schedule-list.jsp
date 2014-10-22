<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="scheduleGrid"></div>
<script type="text/javascript" src="<c:url value='/js/monitor/scheduleManager.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/monitor/validateCronExprUtil.js' />"></script>

<script>
	$(function(){
		var columns = [ 
					{
						title : "任务名称",
						name : "schedulerName",
						width : 150
					},
					{
						title : "定时器标识",
						name : "triggerName",
						width : 150
					},
					{
						title : "执行时间表达式",
						name : "cronExpr",
						width : 250,
                        render: function(item, name, index){
                            return '<input class="form-control cronExpr" value="'+item[name]+'"/>' +
                                    '<button class="btn btn-default" onclick="scheduleManager.updateCronExpr(\''+item.triggerName+'\', this)">更新</button>'
                        }
					},
					{
						title : "上一次执行时间",
						name : "lastBeginRunTime",
						width : 150
					},
                    {
                        title : "是否启用",
                        name : "active",
                        width : 100,
                        align: 'center',
                        render: function(item, name, index){
                            return item[name] ? '<span class="glyphicon glyphicon-ok"></span>':'<span class="glyphicon glyphicon-remove"></span>';
                        }
                    },
                    {
                        title : "当前状态",
                        name : "running",
                        width : 150,
                        render: function(item, name, index){
                            return item[name] ? '运行中':'空闲中';
                        }
                    },
                    {
                        title : "操作",
                        name : "active",
                        width : 80,
                        render: function(item, name, index){
                            return  '<button class="btn btn-default" onclick="scheduleManager.changeStatus(\''+item.triggerName+'\', '+item[name]+')">'+(item[name]? '停用':'启用')+'</button>';
                        }

                    }
				];
		var buttons = [
			{
                content: '<a class="time-example" title="点击查看时间表达式实例"><span class="glyphicon glyphicon-question-sign"></span>&nbsp;时间表达式实例</a>',
                action: 'showTimeExample'
            }
		];
		$.get(contextPath + '/monitor/ServiceMonitor/queryAllSchedulers.koala').done(function(data){
			$('#scheduleGrid').grid({
			 identity: 'triggerName',
             columns: columns,
             buttons: buttons,
             isShowIndexCol: false,
             isShowPages: false,
             isUserLocalData: true,
             localData: data.Rows
        }).on({
        	'showTimeExample': function(){
        		scheduleManager.showTimeExample();
        	}
        });
		});
	})
</script>