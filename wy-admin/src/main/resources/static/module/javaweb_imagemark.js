/**
 * 图片标注信息
 * @auth leavin
 * @date 2021-01-12
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
            , {field: 'imgId', width: 100, title: '图片id', align: 'center'}
            , {field: 'imgDefectType', width: 100, title: '图片缺陷类型', align: 'center'}
            , {field: 'imgDefectLevel', width: 100, title: '图片缺陷等级', align: 'center', templet(d) {
                var cls = "";
                if (d.imgDefectLevel == 1) {
                    // B(高)
                    cls = "layui-btn-normal";
                } else if (d.imgDefectLevel == 2) {
                    // C(中)
                    cls = "layui-btn-danger";
                } else if (d.imgDefectLevel == 3) {
                    // D(低)
                    cls = "layui-btn-warm";
                } 
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.imgDefectLevelName+'</span>';
            }}
            , {field: 'imgDefect', width: 100, title: '图片缺陷(多个规则逗号隔开)', align: 'center'}
            , {field: 'note', width: 100, title: '备注', align: 'center'}
            , {field: 'createUserName', width: 100, title: '添加人', align: 'center'}
            , {field: 'createTime', width: 180, title: '创建时间', align: 'center'}
            /*, {field: 'updateUserName', width: 100, title: '更新人', align: 'center'}
            , {field: 'updateTime', width: 180, title: '更新时间', align: 'center'}*/
            , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList");

        //【设置弹框】
        func.setWin("图片标注信息", 600, 500);

    }
});
