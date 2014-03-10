<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade changePost">
    <div class="modal-dialog" style="width:1060px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">调整岗位</h4>
            </div>
            <div class="modal-body">
                <table>
                    <tr>
                        <td width="25%" style="vertical-align: top;">
                             <div class="tree" id="departmentTree" style="width: 232px;height: 348px; border-top: 2px solid #DDDDDD; margin-right:5px;"></div>
                        </td>
                        <td width="75%" style="vertical-align: top;">
                             <div id="postGrid" class="postGrid"></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="panel panel-default">
                                <div class="panel-heading">任职岗位<span style="color: #428BCA;">(点击设置主岗位)</span></div>
                                <div class="panel-body" id="selectedPost">
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success" id="save">确定</button>
            </div>
        </div>
    </div>
</div>