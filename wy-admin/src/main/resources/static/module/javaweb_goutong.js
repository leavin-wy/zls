/**
 * 沟通记录
 * @auth leavin
 * @date 2023-04-01
 */
layui.use(['func'], function () {

    //声明变量
    var func = layui.func
        , $ = layui.$;

    //【TABLE列数组】
    var cols = [
        {type: 'checkbox', fixed: 'left'}
        , {field: 'id', width: 80, title: 'ID', align: 'center', sort: true, fixed: 'left'}
        /*, {field: 'custId', width: 100, title: '客户id', align: 'center'}*/
        , {field: 'gtTime', width: 160, title: '沟通时间', align: 'center'}
        , {field: 'gtDesc', width: 180, title: '沟通内容', align: 'center'}
        , {field: 'remark', width: 100, title: '备注', align: 'center'}
        , {field: 'createUserName', width: 100, title: '创建人', align: 'center'}
        , {field: 'createTime', width: 160, title: '创建时间', align: 'center'}
        , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
    ];

    //【渲染TABLE】
    var url = "/goutong/list?custId=" + $("#custId").val();
    func.tableIns(cols, "tableList", null, url);

    $(".btnAddGoutong").click(function () {
        func.showWin("添加沟通记录", "/goutong/addGoutong?custId="+$("#custId").val(),600,500);
    });

    $(".btnClose").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.location.reload();//刷新父页面，注意一定要在关闭当前iframe层之前执行刷新
        parent.layer.close(index); //再执行关闭
    });

    //【设置弹框】
    func.setWin("沟通记录", 600, 500);
});
