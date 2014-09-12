<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="user-info" id="user-info">
	    <table class="table table-bordered table-hover">
		<tr>
			<td width="25%">
			  <label class="col-lg-4 control-label">姓名:</label>
			  </td>
	          <td width="25%">
	            <div class="col-lg-8 content" data-id="name"></div>
			</td>
				<td width="25%">
			   <label class="col-lg-4 control-label">用户名称:</label>
			   </td>
	          <td width="25%">
	            <div class="col-lg-8 content" data-id="userAccount">
	            </div>
			</td>
		</tr>
		<tr>
			<td >
			    <label class="col-lg-4 control-label">创建时间:</label>
			    </td>
	          <td >
	            <div class="col-lg-8 content" data-id="createDate">
	            </div>
			</td>
			<td >
                    <label class="col-lg-4 control-label">描述:</label>
                    </td>
	          <td >
                    <div class="col-lg-8 content" data-id="description" >

                    </div>
                </td>
		</tr>
		<tr>
			<td >
                <label class="col-lg-4 control-label">邮箱:</label>
                </td>
	          <td >
                <div class="col-lg-8 content" data-id="email">
                </div>
                <a data-target="#changeEmailOfUser" onclick="" class="glyphicon glyphicon-pencil">修改</a>
			</td>
			<td >
			    <label class="col-lg-4 control-label">联系电话:</label>
			    </td>
	          <td >
	            <div class="col-lg-8 content" data-id="telePhone">
	            </div>
	            <a  data-target="#changeTelePhoneOfUser" onclick="" class="glyphicon glyphicon-pencil">修改</a>
			</td>
		</tr>
		<tr>
			<td >
			    <label class="col-lg-4 control-label">是否可用:</label>
			    </td>
	          <td colspan="3">
	            <div class="col-lg-8">
	                <div class="col-lg-8 content" data-id="disabled" >
	                </div>
	            </div>
			</td>
		</tr>
	</table>

</div>
<script>
$(function(){
	var script = document.createElement('script');
    script.src = contextPath + '/js/security/user-info.js';
    document.getElementById('user-info').parentNode.appendChild(script);
    
    
   /* -------------------修改邮箱--------------------  */ 
    $('#user-info').find("a[data-target=#changeEmailOfUser]").click(function(){
    	 $.get(contextPath + '/pages/auth/user-changeEmail.jsp').done(function(data){
             var dialog  = $(data);
             var oldEmail = dialog.find('#oldEmail');
             var newEmail = dialog.find('#newEmail');
             var confirmPassword = dialog.find('#confirmPassword');
             dialog.find('#changeEmailOfUserSave').on('click', function(){
                 var data = {};
                 data['email'] = newEmail.val();
                 data['userPassword'] = confirmPassword.val();
                 $.post(contextPath+'/auth/currentUser/changeUserEmail.koala',data,function(data){
                     if(data.success){
                         dialog.find('#changeEmailOfUserMessage').message({
                             type : 'success',
                             content : '更改邮箱成功!'
                         });
                        window.location.href=contextPath+"/index.koala";
                     }else{
                         dialog.find('#changeEmailOfUserMessage').message({
                             type : 'error',
                             content : data.errorMessage
                         });
                     }
                 });
             }).end().modal({
                 keyboard : false
             }).on({
                 'hidden.bs.modal' : function() {
                     $(this).remove();
                 }
             });
             //兼容IE8 IE9
             if(window.ActiveXObject){
                 if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                     dialog.trigger('shown.bs.modal');
                 }
             }
           
            });
    });
    /* -------------------修改电话--------------------  */ 
});
$(function(){
    $('#user-info').find("a[data-target=#changeTelePhoneOfUser]").click(function(){
        $.get(contextPath + '/pages/auth/user-changeTelePhone.jsp').done(function(data){
            var dialog  = $(data);
            var oldTelePhone = dialog.find('#oldTelePhone');
            var newTelePhone = dialog.find('#newTelePhone');
            var confirmPassword = dialog.find('#confirmPassword');

            dialog.find('#changeTelePhoneOfUserSave').on('click', function(){
                var data = {};
                data['telePhone'] = newTelePhone.val();
                data['userPassword'] = confirmPassword.val();
                $.post(contextPath+'/auth/currentUser/changeUserTelePhone.koala',data,function(data){
                    if(data.success){
                        dialog.find('#changeTelePhoneOfUserMessage').message({
                            type : 'success',
                            content : '更改联系电话成功!'
                        });
                        window.location.href=contextPath+"/index.koala";
                    }else{
                        dialog.find('#changeTelePhoneOfUserMessage').message({
                            type : 'error',
                            content : data.errorMessage
                        });
                    }
                });
            }).end().modal({
                keyboard : false
            }).on({
                'hidden.bs.modal' : function() {
                    $(this).remove();
                }
            });
            //兼容IE8 IE9
            if(window.ActiveXObject){
                if(parseInt(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/)[1]) < 10){
                    dialog.trigger('shown.bs.modal');
                }
            }
        });
    });
});
</script>
