<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<form id="positionSearchForm" target="_self" class="form-horizontal searchCondition">
	<div id="positionQueryDiv" hidden="true">
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		  <td>
	          <div class="form-group">
	              <label class="control-label" style="width:100px;float:left;">岗位编号:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="sn" class="form-control" type="text" style="width:180px;"/>
	              </div>
	
	              <label class="control-label" style="width:100px;float:left;">岗位名称:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="name" class="form-control" type="text" style="width:180px;"/>
	              </div>
	              
	              <label class="control-label" style="width:100px;float:left;">岗位描述:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="description" class="form-control" type="text" style="width:180px;"/>
	              </div>
	          </div>
	      </td>
	      <td style="vertical-align: bottom;"><button id="positionSearchBtn" type="button" style="position:relative; margin-left:35px; top: -15px" class="btn btn-success"><span class="glyphicon glyphicon-search"></span>&nbsp;</button></td>
	  </tr>
	</table>	
	</div>
</form>

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
            {content: '<ks:hasSecurityResource identifier="postManagerCreate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;创建</button></ks:hasSecurityResource>', action: 'add'},
            {content: '<ks:hasSecurityResource identifier="postManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button></ks:hasSecurityResource>', action: 'modify'},
            {content: '<ks:hasSecurityResource identifier="postManagerDelete"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;撤销</button></ks:hasSecurityResource>', action: 'delete'},
            {content: '<ks:hasSecurityResource identifier="postManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索<span class="caret"></span></button></ks:hasSecurityResource>', action: 'search'}
        ];
        $('#positionGrid').grid({
             identity: 'id',
             columns: cols,
             buttons: buttons,
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
                    },
					'search' : function() {						
						$("#positionQueryDiv").slideToggle("slow");						 
					}
        })
        var form = $("#positionSearchForm");
        form.find('#positionSearchBtn').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
            $("#positionGrid").getGrid().search(params);
        });
    })
</script>