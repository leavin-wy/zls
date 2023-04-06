/**
 * 图片分组信息
 * @auth leavin
 * @date 2021-01-11
 */
layui.use(['func'], function () {

    //声明变量
    var func = layui.func
        , $ = layui.$;

    if (A == 'index') {
        //【TABLE列数组】
        var cols = [
              {type: 'checkbox', fixed: 'left'}
            , {field: 'id', width: 80, title: 'ID', align: 'center', sort: true, fixed: 'left'}
            , {field: 'groupName', width: 200, title: '分组名称', align: 'center'}
            , {field: 'imageNum', width: 100, title: '图片数量', align: 'center'}
            , {field: 'ismarkNum', width: 100, title: '已标注', align: 'center'}
            , {field: 'nomarkNum', width: 100, title: '未标注', align: 'center'}
            , {field: 'createUserName', width: 100, title: '添加人', align: 'center'}
            , {field: 'createTime', width: 180, title: '创建时间', align: 'center'}
            , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList");

        //【设置弹框】
        func.setWin("图片分组信息", 600, 500);

    }
});
