/**
 * 客资指标结果
 * @auth leavin
 * @date 2023-04-07
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
            , {field: 'userId', width: 100, title: '用户编号', align: 'center'}
            , {field: 'userName', width: 100, title: '用户名', align: 'center'}
            , {field: 'targetCode', width: 100, title: '指标代码', align: 'center'}
            , {field: 'targetName', width: 100, title: '指标名称', align: 'center'}
            , {field: 'targetValue', width: 100, title: '指标值', align: 'center'}
            , {field: 'dataTime', width: 180, title: '数据日期', align: 'center'}
            , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList");

        //【设置弹框】
        func.setWin("客资指标结果", 600, 500);

    }
});
