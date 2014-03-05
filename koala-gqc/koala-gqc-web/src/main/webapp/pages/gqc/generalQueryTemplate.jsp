<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade">
    <div class="modal-dialog general-query-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">通用查询配置</h4>
            </div>
            <div class="modal-body">
                <div class="generalQuery">
                    <div class="form-inline">
                        <div class="form-group">
                            <span class="control-label">数据源:</span>
                            <div class="btn-group select" id="dataSourceSelect"></div>
                        </div>
                        <div class="form-group">
                            <span class="control-label">表:</span>
                            <div class="btn-group select" id="tableSelect"> </div>
                        </div>
                        <div class="form-group">
                            <span class="control-label">查询器名称:</span>
                            <div class="btn-group">
                                <input type="text" class="form-control queryName" id="generalQuery_queryName" required=true  required=true rgExp='/^[a-zA-Z0-9_]{1,}$/' data-content="只能输入数字、字母及下划线" placeholder="只能输入数字、字母及下划线"/>
                                <span class="required">*</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <span class="control-label">描述:</span>
                            <div class="btn-group">
                                <input type="text" class="form-control" id="generalQuery_description" placeholder="描述"/>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="glyphicon glyphicon-th"></span>&nbsp;<label>添加静态查询条件</label></div>
                        <table class="table">
                            <tr>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">可选列</div>
                                        <table class="table table-responsive table-bordered grid query-condition">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered">
                                                              <th class="column-name">
                                                                  列名
                                                              </th>
                                                              <th class="operation">
                                                                  操作
                                                              </th>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped" id="staticQueryLeftTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">已选列</div>
                                        <table class="table table-responsive table-bordered grid staticQuerySelected">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered" >
                                                                <tr>
                                                                    <th class="column-name">
                                                                        列名
                                                                    </th>
                                                                    <th class="query-operation">
                                                                        条件
                                                                    </th>
                                                                    <th class="value">
                                                                        值
                                                                    </th>
                                                                    <th class="visibility">
                                                                        是否可见
                                                                    </th>
                                                                    <th class="delete-btn">
                                                                        操作
                                                                    </th>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped staticQueryRightTable" id="staticQueryRightTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="glyphicon glyphicon-th"></span>&nbsp;<label>添加动态查询条件</label></div>
                        <table class="table">
                            <tr>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">可选列</div>
                                        <table class="table table-responsive table-bordered grid query-condition">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered">
                                                                <th class="column-name">
                                                                    列名
                                                                </th>
                                                                <th class="operation">
                                                                    操作
                                                                </th>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped" id="dynamicQueryLeftTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">已选列</div>
                                        <table class="table table-responsive table-bordered grid dynamicQuerySelected">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered" >
                                                                <tr>
                                                                    <th class="column-name">
                                                                        列名
                                                                    </th>
                                                                    <th class="show-label">
                                                                        页面显示名称
                                                                    </th>
                                                                    <th class="widgetType">输入框类型</th>
                                                                    <th class="query-operation">条件</th>
                                                                    <th class="delete-btn">操作</th>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped" id="dynamicQueryRightTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading"><span class="glyphicon glyphicon-th"></span>&nbsp;<label>添加显示列</label></div>
                        <table class="table">
                            <tr>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">可选列</div>
                                        <table class="table table-responsive table-bordered grid query-condition">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered">
                                                                <th class="column-name">
                                                                    列名
                                                                </th>
                                                                <th class="operation">
                                                                    操作
                                                                </th>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped" id="showColumnLeftTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div class="panel panel-default table-responsive">
                                        <div class="panel-heading">已选列</div>
                                        <table class="table table-responsive table-bordered grid showColumnSelected">
                                            <tr>
                                                <td>
                                                    <div class="grid-body">
                                                        <div class="grid-table-head">
                                                            <table class="table table-bordered" >
                                                                <tr>
                                                                    <th class="column-name">
                                                                        列名
                                                                    </th>
                                                                    <th class="show-label">
                                                                        显示列名称
                                                                    </th>
                                                                    <th class="delete-btn">操作</th>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <div class="grid-table-body">
                                                            <table class="table table-bordered table-hover table-striped" id="showColumnRightTable">

                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-success" id="generalQuerySave">保存</button>
            </div>
        </div>
    </div>
</div>
