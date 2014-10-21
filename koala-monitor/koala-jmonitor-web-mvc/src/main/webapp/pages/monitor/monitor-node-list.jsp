<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="monitorNodeGrid"></div>
<script type="text/javascript" src="<c:url value='/js/monitor/monitorNodeManager.js' />"></script>
<script>
	$(function(){
		var columns = [ 
					{
						title : "监控节点标识",
						name : "nodeId",
						width : 120
					},
					{
						title : "监控节点名称",
						name : "nodeName",
						width : 150
					},
					{
						title : "监控节点URL",
						name : "nodeUri",
						width : 200
					},
					{
						title : "监控内容",
						name : "conponents",
						width : 350,
                        render: function(item, name, index){
                            var conponents = item[name];
                            var htmls = [];
                            $.each(conponents, function(childIndex){
                                htmls.push('<a onclick="monitorNodeManager.updateMonitorConfig('+childIndex+', '+index+')">'+this.name+'<span class="glyphicon glyphicon-'
                                        +(this.active ? 'ok':'remove')+'"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                            });
                            return htmls.join('');
                        }
					},
                    {
                        title : "最后响应时间",
                        name : "latestSessionTime",
                        width : 140
                    },
                    {
                        title : "操作",
                        name : "active",
                        width : 120,
                        render: function(item, name, index){
                            return  '<a onclick="monitorNodeManager.pageServerSummryInfo(\''+item.nodeId+'\')"><span class="glyphicon glyphicon-sd-video"></span>&nbsp;进入控制台</a>';
                        }

                    }
				];
		var buttons = [
			{
                content: '<button class="btn btn-info"><span class="glyphicon glyphicon-transfer"></span>&nbsp;同步监控数据配置</button>',
                action: 'openSyncDataConfDig'
            }
		];
		$.get(contextPath + '/monitor/NodeInfo/queryAllNodes.koala?onlyActive=y').done(function(data){
				$('#monitorNodeGrid').grid({
					 identity: 'nodeId',
		             columns: columns,
		             buttons: buttons,
		             isShowIndexCol: false,
		             isShowPages: false,
		             isUserLocalData: true,
             		 localData: data.Rows
		        }).on({
		        	'openSyncDataConfDig': function(){
		                monitorNodeManager.openSyncDataConfDig($(this));
		        	}
		        });
		});
	});
</script>