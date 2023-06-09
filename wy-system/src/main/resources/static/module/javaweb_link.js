/**
 * 友链
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
              {type: 'checkbox', fixed: 'left'}
            , {field: 'id', width: 80, title: 'ID', align: 'center', sort: true, fixed: 'left'}
            , {field: 'name', width: 200, title: '友链名称', align: 'center'}
            , {field: 'type', width: 100, title: '类型', align: 'center', templet(d) {
                var cls = "";
                if (d.type == 1) {
                    // 友情链接
                    cls = "layui-btn-normal";
                } else if (d.type == 2) {
                    // 合作伙伴
                    cls = "layui-btn-danger";
                }
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.typeName+'</span>';
            }}
            , {field: 'url', width: 200, title: '友链地址', align: 'center', templet(d) {
                return "<a href='" + d.url + "' target='_blank'>" + d.url + "</a>";
                }}
            , {field: 'itemName', width: 150, title: '站点ID', align: 'center'}
            , {field: 'cateName', width: 200, title: '栏目ID', align: 'center'}
            , {field: 'platform', width: 100, title: '平台', align: 'center', templet(d) {
                var cls = "";
                if (d.platform == 1) {
                    // PC站
                    cls = "layui-btn-normal";
                } else if (d.platform == 2) {
                    // WAP站
                    cls = "layui-btn-danger";
                } else if (d.platform == 3) {
                    // 微信小程序
                    cls = "layui-btn-warm";
                } else if (d.platform == 4) {
                    // APP应用
                    cls = "layui-btn-primary";
                }
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.platformName+'</span>';
            }}
            , {field: 'form', width: 100, title: '友链形式', align: 'center', templet(d) {
                var cls = "";
                if (d.form == 1) {
                    // 文字链接
                    cls = "layui-btn-normal";
                } else if (d.form == 2) {
                    // 图片链接
                    cls = "layui-btn-danger";
                }
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.formName+'</span>';
            }}
            , {field: 'image', width: 100, title: '友链图片', align: 'center', templet: function (d) {
                var imageStr = "";
                if (d.imageUrl) {
                    imageStr = '<a href="' + d.imageUrl + '" target="_blank"><img src="' + d.imageUrl + '" height="26" /></a>';
                }
                return imageStr;
              }
            }
            , {field: 'status', width: 100, title: '状态', align: 'center', templet: '#statusTpl'}
            , {field: 'sort', width: 100, title: '显示顺序', align: 'center'}
            , {field: 'createUserName', width: 100, title: '添加人', align: 'center'}
            , {field: 'createTime', width: 180, title: '添加时间', align: 'center'}
            , {field: 'updateUserName', width: 100, title: '更新人', align: 'center'}
            , {field: 'updateTime', width: 180, title: '更新时间', align: 'center'}
            , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList");

        //【设置弹框】
        func.setWin("友链", 750, 630);

        //【设置状态】
        func.formSwitch('status', null, function (data, res) {
            console.log("开关回调成功");
        });
    }
});
