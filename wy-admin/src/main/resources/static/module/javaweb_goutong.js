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
        {width: 60, title: '序号', type:'numbers'}
        , {field: 'custName', width: 100, title: '客户名', align: 'center'}
        , {field: 'gtTime', width: 160, title: '沟通时间', align: 'center'}
        , {field: 'gtDesc', width: 180, title: '沟通内容', align: 'center'}
        , {field: 'replyFlagStr', width: 120, title: '回复标志', align: 'center'}
        , {field: 'interactTime', width: 120, title: '下次沟通时间', align: 'center'}
        , {field: 'interactDesc', width: 180, title: '下次沟通内容', align: 'center'}
        , {field: 'createUserName', width: 100, title: '创建人', align: 'center'}
        , {field: 'createTime', width: 160, title: '创建时间', align: 'center'}
    ];

    //【渲染TABLE】
    var url = "/goutong/list?custId=" + $("#custId").val();
    func.tableIns(cols, "tableList", null, url);


    //【设置弹框】
    func.setWin("沟通记录", 600, 500);
});
