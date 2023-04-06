/**
 * 图片信息
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
            /*, {field: 'imgUrl', width: 200, title: '图片', align: 'center'}*/
            , {field: 'imgUrl', width: 100, title: '图片', align: 'center', templet: function (d) {
                    var avatarStr = "";
                    if (d.avatarUrl) {
                        avatarStr = '<a href="' + d.avatarUrl + '" target="_blank"><img src="' + d.avatarUrl + '" height="26" /></a>';
                    }
                    return avatarStr;
                }
            }
            , {field: 'imgGroupName', width: 150, title: '图片分组', align: 'center'}
            /*, {field: 'imgLevel', width: 100, title: '图片等级', align: 'center', templet(d) {
                var cls = "";
                if (d.imgLevel == 1) {
                    // A级
                    cls = "layui-btn-normal";
                } else if (d.imgLevel == 2) {
                    // B级
                    cls = "layui-btn-danger";
                } else if (d.imgLevel == 3) {
                    // C级
                    cls = "layui-btn-warm";
                } else if (d.imgLevel == 4) {
                    // D级
                    cls = "layui-btn-primary";
                }
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.imgLevelName+'</span>';
            }}
            , {field: 'imgDefect', width: 100, title: '图片缺陷', align: 'center'}
            , {field: 'note', width: 100, title: '备注', align: 'center'}*/
            /*, {field: 'sort', width: 100, title: '显示顺序', align: 'center'}*/
            , {field: 'createUserName', width: 100, title: '添加人', align: 'center'}
            , {field: 'createTime', width: 180, title: '创建时间', align: 'center'}
            , {field: 'updateUserName', width: 100, title: '更新人', align: 'center'}
            , {field: 'updateTime', width: 180, title: '更新时间', align: 'center'}
            , {fixed: 'right', width: 150, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList");

        $(".btnUpload").click(function () {
            func.showWin("上传图片", "/images/uploadPage");
        });

        //【设置弹框】
        func.setWin("图片信息", 500, 300);

    }
});
