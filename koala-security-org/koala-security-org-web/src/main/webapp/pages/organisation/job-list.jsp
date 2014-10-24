<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>

<form id="jobSearchForm" target="_self" class="form-horizontal searchCondition">
	<div id="jobQueryDiv" hidden="true">
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		  <td>
	          <div class="form-group">
	              <label class="control-label" style="width:100px;float:left;">职务编号:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="sn" class="form-control" type="text" style="width:180px;"/>
	              </div>
	
	              <label class="control-label" style="width:100px;float:left;">职务名称:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="name" class="form-control" type="text" style="width:180px;"/>
	              </div>
	              
	              <label class="control-label" style="width:100px;float:left;">职务描述:&nbsp;</label>
	              <div style="margin-left:15px;float:left;">
	                  <input name="description" class="form-control" type="text" style="width:180px;"/>
	              </div>
	          </div>
	      </td>
	      <td style="vertical-align: bottom;"><button id="jobSearchBtn" type="button" style="position:relative; margin-left:35px; top: -15px" class="btn btn-success"><span class="glyphicon glyphicon-search"></span>&nbsp;</button></td>
	  </tr>
	</table>	
	</div>
</form>

<div id="jobgrid"></div>
<script type="text/javascript" src="<c:url value='/js/organisation/job.js' />"></script>

<script>
    $(function(){
       var cols = [
            { title:'职务编号', name:'sn' , width: '250px'},
            { title:'职务名称', name:'name', width: '250px'},
            { title:'职务描述', name:'description' , width: 'auto'}
        ];
        var buttons = [
            {content: '<ks:hasSecurityResource identifier="jobManagerCreate"><button class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>&nbsp;创建</button></ks:hasSecurityResource>', action: 'add'},
            {content: '<ks:hasSecurityResource identifier="jobManagerUpdate"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-edit"></span>&nbsp;修改</button></ks:hasSecurityResource>', action: 'modify'},
            {content: '<ks:hasSecurityResource identifier="jobManagerDelete"><button class="btn btn-danger" type="button"><span class="glyphicon glyphicon-remove"></span>&nbsp;撤销</button></ks:hasSecurityResource>', action: 'delete'},
            {content: '<ks:hasSecurityResource identifier="jobManagerQuery"><button class="btn btn-success" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;高级搜索<span class="caret"></span></button></ks:hasSecurityResource>', action: 'search'}
        ];
        $('#jobgrid').grid({
             identity: 'id',
             columns: cols,
             buttons: buttons,
             url: contextPath +'/job/pagingquery.koala'
        }).on({
            'add': function(){
                job().add( $(this));
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
                job().modify(indexs[0], $this);
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
                    callBack: function(){ job().del(data.item, $this);}
                });
            },
			'search' : function() {						
				$("#jobQueryDiv").slideToggle("slow");						 
			}
        })
        var form = $("#jobSearchForm");
        form.find('#jobSearchBtn').on('click', function(){
            var params = {};
            form.find('.form-control').each(function(){
                var $this = $(this);
                var name = $this.attr('name');
                 if(name){
                    params[name] = $this.val();
                }
            });
            $("#jobgrid").getGrid().search(params);
        });
    })
</script>