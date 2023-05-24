/**
 * 客户信息
 * @auth leavin
 * @date 2023-03-31
 */
layui.use(['func'], function () {

    //声明变量
    var func = layui.func
        , $ = layui.$
        ,l = layui.layer;

    if (A == 'index') {
        //【TABLE列数组】
        var cols = [
              /*{type: 'checkbox', fixed: 'left'}
            , */{field: 'id', width: 60, title: 'ID', align: 'center', sort: true, fixed: 'left'}
            , {field: 'custName', width: 90, title: '客户名称', align: 'center', fixed: 'left'}
            , {field: 'nickName', width: 90, title: '客户昵称', align: 'center', fixed: 'left'}
            , {field: 'sex', width: 80, title: '性别', align: 'center', templet(d) {
                var cls = "";
                if (d.sex == 1) {
                    // 男,2
                    cls = "layui-btn-normal";
                } else if (d.sex == 2) {
                    cls = "layui-btn-danger";
                }
				return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.sexName+'</span>';
            }}
            , {field: 'birthday', width: 120, title: '出生时间', align: 'center'}
            , {field: 'age', width: 100, title: '年龄', align: 'center'}
            , {field: 'source', width: 100, title: '渠道', align: 'center', templet(d) {
                    var cls = "";
                    if (d.source == 1) {
                        cls = "layui-btn-normal";
                    } else if (d.source == 2) {
                        cls = "layui-btn-danger";
                    }else if (d.source == 3) {
                        cls = "layui-btn-warm";
                    }else if (d.source == 4) {
                        cls = "layui-btn-primary";
                    }else if (d.source == 5) {
                        cls = "layui-btn layui-btn-primary layui-border_black";
                    }else if (d.source == 6) {
                        cls = "layui-btn-warm";
                    }else if (d.source == 7) {
                        cls = "layui-btn-danger";
                    }
                    return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.sourceName+'</span>';
                }}
            , {field: 'custType', width: 100, title: '客户类型', align: 'center', templet(d) {
                var cls = "";
                if (d.custType == 1) {
                    cls = "layui-btn-normal";
                } else if (d.custType == 2) {
                    cls = "layui-btn-danger";
                }else if (d.custType == 3) {
                    cls = "layui-btn-warm";
                }else if (d.custType == 4) {
                    cls = "layui-btn-primary";
                }else if (d.custType == 5) {
                    cls = "layui-btn-disabled";
                }else if (d.custType == 6) {
                    cls = "layui-btn-danger";
                }else if (d.custType == 7) {
                    cls = "layui-btn-warm";
                }
                    return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.custTypeName+'</span>';
            }}
            , {field: 'lastTandianTimeStr', width: 160, title: '最近探店时间', align: 'center'}
            , {field: 'tandianNum', width: 100, title: '探店次数', align: 'center'}
            , {field: 'lastGoutongTimeStr', width: 160, title: '最近沟通时间', align: 'center'}
            , {field: 'lastGoutongDesc', width: 200, title: '最近沟通内容', align: 'left'}
            , {field: 'replyFlag', width: 80, title: '是否回复', align: 'center', templet(d) {
                    var cls = "";
                    if (d.replyFlag == 1) {
                        // 1,未回复
                        cls = "layui-btn-normal";
                    } else if (d.replyFlag == 2) {
                        //已回复
                        cls = "layui-btn-danger";
                    }
                    return '<span class="layui-btn ' + cls + ' layui-btn-xs">'+d.replyFlagName+'</span>';
                }}
            , {field: 'interactTimeStr', width: 120, title: '下次沟通时间', align: 'center'}
            , {field: 'interactDesc', width: 120, title: '下次沟通内容', align: 'center'}
            , {field: 'phone', width: 120, title: '联系电话', align: 'center'}
            , {field: 'address', width: 180, title: '家庭住址', align: 'center'}
            , {field: 'inviteTime', width: 120, title: '邀约时间', align: 'center'}
            , {field: 'completeTime', width: 120, title: '成交时间', align: 'center'}
            , {field: 'createUserName', width: 100, title: '创建人', align: 'center'}
            , {field: 'createTime', width: 160, title: '创建时间', align: 'center'}
            , {fixed: 'right', width: 330, title: '功能操作', align: 'center', toolbar: '#toolBar'}
        ];

        //【渲染TABLE】
        func.tableIns(cols, "tableList", function (layEvent, data) {
            var id = data.id;
            if (layEvent === 'tandian') {
                func.showWin("探店记录", "/tandian/indexTd?custId="+id,1000,600);
            }
            if (layEvent === 'goutong') {
                func.showWin("沟通记录", "/goutong/indexGt?custId="+id,1000,600);
            }
        })

        $(".btnDownload").click(function () {
            let data = layui.form.val(`custForm`);
            var str = "";
            for (var val in data) {
                str += "&"+val+"="+data[val];
            }
            var url = "/customer/custExport";
            if(str.length>1){
                url += "?"+str.substr(1);
            }
            //console.info(url);
            layer.confirm('您确定要下载客户信息吗？', {icon: 3, title: '下载提示'}, function (index) {
                window.location.href = url;
                var msgId = layer.msg('正在下载，请稍候···', {
                    icon: 16,
                    shade: 0.4,
                    time: 1500
                });
            });
        });

        //上传excel
        func.uploadExcel({
            done:function (res) {
                var data = res.data;
                if(data != null && data.length > 0){
                    openErrorInfo(data);
                }
                func.tableIns(cols, "tableList", function (layEvent, data) {
                    var id = data.id;
                    if (layEvent === 'tandian') {
                        func.showWin("探店记录", "/tandian/indexTd?custId="+id,1000,600);
                    }
                    if (layEvent === 'goutong') {
                        func.showWin("沟通记录", "/goutong/indexGt?custId="+id,1000,600);
                    }
                })
            }
        });

        function openErrorInfo(data) {
            var html = '<table class="layui-table">';
            html+="<tr> <td>客户名称</td> <td>客户昵称</td> " +
                "<td>性别</td><td>出生时间</td><td>客户类型</td>" +
                "<td>渠道</td><td>错误信息</td></tr>";
            for(var i = 0;i< data.length;i++){
                html+="<tr>";
                html+="<td>"+data[i].custName+"</td>";
                html+="<td>"+data[i].nickName+"</td>";
                html+="<td>"+data[i].sexName+"</td>";
                html+="<td>"+data[i].birthdayStr+"</td>";
                html+="<td>"+data[i].sourceName+"</td>";
                html+="<td>"+data[i].sourceName+"</td>";
                html+="<td style='color: red'>"+data[i].errorMsg+"</td>";
                html+="</tr>";
            }
            html+="</table>";
            l.open({
                type: 1,
                id: "larry_theme_R",
                title: "导入失败的客户信息",
                content:html,
                area:['1000px','500px'],
                btn:'关闭',
                offset:'auto',
                shade:0,
            });
        }

        //【设置弹框】
        func.setWin("客户信息",900, 570);

    }
});
