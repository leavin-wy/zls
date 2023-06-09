/**
 * 城市
 * @auth leavin
 * @date 2020-05-03
 */
layui.use(['func'], function () {

    //声明变量
    var func = layui.func
        , $ = layui.$;

    if (A == 'index') {
        //【TABLE列数组】
        var cols = [
              {field: 'id', width: 80, title: 'ID', align: 'center', sort: true}
            , {field: 'name', width: 200, title: '城市名称', align: 'left'}
            , {field: 'level', width: 100, title: '城市级别', align: 'center', templet(d) {
                var cls = "";
                if (d.level == 1) {
                    // 省份
                    cls = "layui-btn-normal";
                } else if (d.level == 2) {
                    // 市区
                    cls = "layui-btn-danger";
                } else if (d.level == 3) {
                    // 区县
                    cls = "layui-btn-warm";
                } 
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.levelName+'</span>';
            }}
            , {field: 'citycode', width: 150, title: '城市编号（区号）', align: 'center'}
            , {field: 'pAdcode', width: 150, title: '父级地理编号', align: 'center'}
            , {field: 'adcode', width: 150, title: '地理编号', align: 'center'}
            , {field: 'sort', width: 100, title: '排序号', align: 'center'}
            , {field: 'createUserName', width: 100, title: '添加人', align: 'center'}
            , {field: 'createTime', width: 180, title: '创建时间', align: 'center'}
            , {width: 200, title: '功能操作', align: 'left', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.treetable(cols, "tableList");

        //【设置弹框】
        func.setWin("城市",700, 400);

    }
});
