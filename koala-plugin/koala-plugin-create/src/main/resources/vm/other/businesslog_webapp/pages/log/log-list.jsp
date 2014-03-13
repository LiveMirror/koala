   <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <!-- search form -->
    <form id="logGrid" class="form-horizontal">
        <table>
            <tr>
                <td>
                    <div class="form-group">
                        <label class="control-label" style="width:100px;float:left;">日志类型:&nbsp;</label>
                        <div style="width:180px;float:left;">
                            <input name="category" class="form-control" type="text" style="width:180px;" id="category"  />
                        </div>
                        <label class="control-label" style="width:100px;float:left;">用户名:&nbsp;</label>
                        <div style="width:180px;float:left;">
                            <input name="user" class="form-control" type="text" style="width:180px;" id="user"  />
                        </div>
                        <label class="control-label" style="width:100px;float:left;">IP:&nbsp;</label>
                        <div style="width:180px;float:left;">
                            <input name="ip" class="form-control" type="text" style="width:180px;" id="ip"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label" style="width:100px;float:left;">日志内容:&nbsp;</label>
                        <div style="width:180px;float:left;">
                            <input name="log" class="form-control" type="text" style="width:180px;" id="log"  />
                        </div>
                        <label class="control-label" style="width:100px;float:left;">操作时间:&nbsp;</label>
                        <div style="width:480px;float:left;">
                            <div class="input-group date form_datetime" style="width:160px;float:left;" >
                                <input type="text" class="form-control" style="width:160px;" name="time" id="time" >
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                            <div style="float:left; width:10px; margin-left:auto; margin-right:auto;">&nbsp;-&nbsp;</div>
                            <div class="input-group date form_datetime" style="width:160px;float:left;" >
                                <input type="text" class="form-control" style="width:160px;" name="timeEnd" id="timeEnd" >
                                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                            </div>
                        </div>
                    </div>
                </td>
                <td style="vertical-align: bottom;"><button id="search" type="button" style="position:relative; top: -15px" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button></td>
            </tr>
        </table>
    </form>
<!-- grid -->
<div id="logGrid"></div>
<script>
$(function(){
    var cols = [
        {title:'日志类型', name:'category', width: '180'},
        { title:'用户', name:'user' , width: '120'},
        { title:'IP', name:'ip' , width: '200'},
        { title:'日志内容', name:'log' , width: '200'},
        { title:'操作时间', name:'time' , width: '200'}
    ];
    var grid = $('#logGrid');
    grid.grid({
        identity: 'id',
        columns: cols,
        isShowIndexCol: false,
        url: contextPath + '/log/list.koala'
    });
    var form = $('#logGrid');
    var startTimeVal = form.find('#time');
    var startTime = startTimeVal.parent();
    var endTimeVal = form.find('#timeEnd');
    var endTime = endTimeVal.parent();
    startTime.datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        pickerPosition: 'bottom-left'
    }).on('changeDate', function(){
          endTime.datetimepicker('setStartDate', startTimeVal.val());
    });//加载日期选择器
    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    startTime.datetimepicker('setDate', yesterday);
    endTime.datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: true,
        todayBtn: true,
        minView: 2,
        pickerPosition: 'bottom-left'
    }).on('changeDate', function(ev){
        startTime.datetimepicker('setEndDate', endTimeVal.val());
    }).datetimepicker('setDate', new Date()).trigger('changeDate');//加载日期选择器
    form.find('#search').on('click', function(){
        if(!Validator.Validate(form[0],3))return;
        var params = {};
        form.find('input').each(function(){
            var $this = $(this);
            var name = $this.attr('name');
            if(name){
                params[name] = $this.val();
            }
        });
        grid.getGrid().search(params);
    });
})
</script>