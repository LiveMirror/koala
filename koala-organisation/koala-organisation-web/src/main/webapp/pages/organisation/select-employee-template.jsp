<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade employeeManager">
    <style>
        .employeeManager .selected-employee {
            background-color: #e6e6e6;
            float: left;
            margin-right: 10px;
            margin-top: 2px;
            padding: 4px 0;
            min-width: 65px;
            text-align: center;
        }
        .employeeManager .selected-employee a {
            position: relative;
            top: -2px;
            right: 2px;
            float: right;
            outline: none;
            text-decoration: none;
            cursor: pointer;
        }
        .employeeManager .modal-body {
            padding-top: 4px!important;
            padding-bottom: 0;;
        }
        .employeeManager .grid-table-body {
        	height:210px;
        }
        .employeeManager .panel {
            margin-top: 5px;
            margin-bottom: 0;
        }
        .employeeManager .panel-body {
            min-height: 38px;
            padding: 6px;
        }
        .employeeManager .tree {
            padding-top: 30;
            height: 325px;
            min-width: 250px;
            border: 1px solid #DDDDDD;
        }
        .employeeManager .employeeGrid {
            height: 320px;
            max-width: 640px;
        }
        .noDepartmentEmployee {
            position: relative;
            top: -7px;
        }
        .noDepartmentEmployee button {
            width: 100%;
        }
    </style>
    <div class="modal-dialog" style="width:960px;  padding-top: 30px;;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">选择员工</h4>
            </div>
            <div class="modal-body">
                <table>
                    <tr>
                        <td width="25%" style="vertical-align: top;">
                             <div class="tree" id="departmentTree"></div>
                             <div class="noDepartmentEmployee"><button class="btn btn-info">未分配部门的员工</button></div>
                        </td>
                        <td width="75%" style="vertical-align: top;">
                             <div id="employeeGrid" class="employeeGrid"></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="panel panel-default">
                                <div class="panel-heading">已选人员</div>
                                <div class="panel-body" id="selectedEmployee">
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