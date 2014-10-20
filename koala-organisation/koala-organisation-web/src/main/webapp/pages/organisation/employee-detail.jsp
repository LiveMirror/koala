<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="employee-detail" id="employeeDetail">
	    <style>
	        .employee-detail {
	            padding-top: 20px!important;
	        }
	        .employee-detail .row .content {
	            position: relative;
	            top: 6px;
	        }
	        .employee-detail .buttons {
	            float: right;
	            padding-right: 2%;
	            padding-top: 5%;
	        }
	        .employee-detail .row > .form-group{
	        	margin-left: 0;
	        	margin-right: 0;
	        	border: 1px solid #DDDDDD;
	        }
	        .employee-detail .control-label {
			    text-align: right;
			}
			.employee-detail .table {
	            border-width: 1px 0 0 1px;
	            border-collapse: separate;
	        }
	        .employee-detail .table th, .employee-detail .table td {
	            border-width: 0 1px 1px 0;
	        }
	        .employee-detail div {
	        	word-wrap: break-word;
	        	word-break: break-all;
	        }
	    </style>
	    <table class="table table-bordered table-hover">
		<tr>
			<td width="50%">
			    <label class="col-lg-4 control-label">编号:</label>
	            <div class="col-lg-8 content" data-id="sn">
	            </div>
			</td>
			<td width="50%">
			  <label class="col-lg-4 control-label">姓名:</label>
	            <div class="col-lg-8 content" data-id="name">
	
	            </div>
			</td>
		</tr>
		<tr>
			<td width="50%">
			   <label class="col-lg-4 control-label">性别:</label>
	            <div class="col-lg-8 content" data-id="gender">
	
	            </div>
			</td>
			<td width="50%">
			    <label class="col-lg-4 control-label">身份证号:</label>
	            <div class="col-lg-8 content" data-id="idNumber">
	
	            </div>
			</td>
		</tr>
		<tr>
			<td width="50%">
			   <label class="col-lg-4 control-label">所属部门:</label>
	            <div class="col-lg-8 content" data-id="organizationName">
	
	            </div>
			</td>
			<td width="50%">
                <label class="col-lg-4 control-label">邮箱:</label>
                <div class="col-lg-8 content" data-id="email">

                </div>
			</td>
		</tr>
        <tr>
                <td width="50%">
                    <label class="col-lg-4 control-label">岗位:</label>
                    <div class="col-lg-8 content" data-id="postName" style="word-wrap: break-word;word-break: break-all;">

                    </div>
                </td>
                <td width="50%">
                    <label class="col-lg-4 control-label" >兼职岗位:</label>
                    <div class="col-lg-8 content" data-id="additionalPostNames" style="word-wrap: break-word;word-break: break-all;">

                    </div>
                </td>
            </tr>
		<tr>
			<td width="50%">
			    <label class="col-lg-4 control-label">移动电话:</label>
	            <div class="col-lg-8 content" data-id="mobilePhone">
	               
	            </div>
			</td>
			<td width="50%">
			    <label class="col-lg-4 control-label">固定电话:</label>
	            <div class="col-lg-8 content" data-id="familyPhone">
	
	            </div>
			</td>
		</tr>
		<tr>
			<td width="50%">
			    <label class="col-lg-4 control-label">入职时间:</label>
	            <div class="col-lg-8">
	                <div class="col-lg-8 content" data-id="entryDate" style="position: relative; right: 4.5%;">
	
	                </div>
	            </div>
			</td>
            <td width="50%"></td>
		</tr>
	</table>
	<div class="form-group row buttons">
		 <button type="button" class="btn btn-default" data-id="returnBtn">关闭</button>
	     <button type="button" class="btn btn-success" data-id="changePostBtn">调整岗位</button>
	</div>
</div>
<script>
$(function(){
	var script = document.createElement('script');
    script.src = contextPath + '/js/organisation/change-post.js';
    document.getElementById('employeeDetail').parentNode.appendChild(script);
    script = document.createElement('script');
    script.src = contextPath + '/js/organisation/employee-detail.js';
    document.getElementById('employeeDetail').parentNode.appendChild(script);
});
</script>

