/**
 * 探店记录
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
        , {field: 'tdTime', width: 180, title: '探店时间', align: 'center'}
        , {field: 'remark', width: 200, title: '备注', align: 'center'}
        , {field: 'createUserName', width: 100, title: '创建人', align: 'center'}
        , {field: 'createTime', width: 180, title: '创建时间', align: 'center'}
        , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
    ];

    //【渲染TABLE】
    var url = "/tandian/list?custId=" + $("#custId").val();
    func.tableIns(cols, "tableList", null, url);

    $(".btnAddTandian").click(function () {
        func.showWin("添加探店记录", "/tandian/addTandian?custId="+$("#custId").val(),600,500);
    });

    $(".btnClose").click(function () {
        parent.reloadTable();//刷新父页面的表格
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        //parent.location.reload();//刷新父页面，注意一定要在关闭当前iframe层之前执行刷新
        parent.layer.close(index); //再执行关闭
    });

    //【设置弹框】
    func.setWin("探店记录", 600, 500);
});
