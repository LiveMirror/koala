<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="permissionDetail">
    <!--
        ========================权限详细信息=========================
         -->
    <table class="table table-bordered table-hover">
        <tr>
            <td colspan="3">
                <label style="font-size:20px;">①权限详细信息</label>
            </td>
        </tr>
        <tr>
            <td width="33.333%">
                <label class="control-label">权限名称:</label>
                <span class="" data-id="permName"></span>
            </td>
            <td width="33.333%">
                <label class="control-label">权限标识:</label>
                <span class="" data-id="permIdentifier" ></span>
            </td>
            <td width="33.333%">
                <label class="control-label">权限描述:</label>
                <span class="" data-id="permDescription"></span>
            </td>
        </tr>
    </table>
    <!--
    ========================菜单=========================
     -->
    <table id="userDetialtoRoles" class="table table-bordered table-hover">
        <tr>
            <td colspan="3">
                <label style="font-size:20px; ">②菜单资源</label>
            </td>
        </tr>
        <tr>
            <td width="30%" colspan="1">
                <label class="control-label">菜单名称:</label>
                <span class="" data-id="menuName"></span>
            </td>
            <td width="30%" colspan="1">
                <label class="control-label">菜单URL:</label>
                <span class="" data-id="menuUrl" ></span>
            </td>
            <td colspan="2">
                <label class="control-label">菜单描述:</label>
                <span class="" data-id="menuDescription"></span>
            </td>
        </tr>
    </table>
    <!--
    ========================URL访问资源=========================
     -->
    <table id="userDetialtoPermissions"  class="table table-bordered table-hover">
        <tr>
            <td colspan="3"><label style="font-size:20px;">③URL访问资源</label></td>
        </tr>
        <tr>
            <td><label>URL名称</label></td>
            <td><label>URL路径</label></td>
            <td><label>URL描述</label></td>
        </tr>
    </table>

    <table id="useDetialtoPermissions"  class="table table-bordered table-hover">
        <tr>
            <td colspan="2"><label style="font-size:20px;">④页面元素资源</label></td>
        </tr>
        <tr>
            <td width="50%"><label>权限名称</label></td>
            <td width="50%"><label>权限描述</label></td>
        </tr>
    </table>

</div>
<script>
    $(function() {
        var permissionId = $('#permissionDetail').parent().attr('data-value')
        $.get(contextPath + '/auth/permission/findInfoOfPermission.koala?permissionId=' + permissionId).done(function (result) {
            var permission = result.data;
            var permissionDetail = $('#permissionDetail');
            permissionDetail.find('[data-id="permName"]').text(permission.name);
            permissionDetail.find('[data-id="permIdentifier"]').text(permission.identifier);
            permissionDetail.find('[data-id="permDescription"]').text(permission.description==null?"":permission.description);

            permissionDetail.find('[data-id="menuName"]').text(permission.menuResource.name);
            permissionDetail.find('[data-id="menuUrl"]').text(permission.menuResource.url);
            permissionDetail.find('[data-id="menuDescription"]').text(permission.menuResource.description==null?"":permission.menuResource.description);

        });
    });
</script>