!function (t) {
    var e = {};

    function n(i) {
        if (e[i]) return e[i].exports;
        var a = e[i] = {i: i, l: !1, exports: {}};
        return t[i].call(a.exports, a, a.exports, n), a.l = !0, a.exports
    }

    n.m = t, n.c = e, n.d = function (t, e, i) {
        n.o(t, e) || Object.defineProperty(t, e, {enumerable: !0, get: i})
    }, n.r = function (t) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(t, "__esModule", {value: !0})
    }, n.t = function (t, e) {
        if (1 & e && (t = n(t)), 8 & e) return t;
        if (4 & e && "object" == typeof t && t && t.__esModule) return t;
        var i = Object.create(null);
        if (n.r(i), Object.defineProperty(i, "default", {
            enumerable: !0,
            value: t
        }), 2 & e && "string" != typeof t) for (var a in t) n.d(i, a, function (e) {
            return t[e]
        }.bind(null, a));
        return i
    }, n.n = function (t) {
        var e = t && t.__esModule ? function () {
            return t.default
        } : function () {
            return t
        };
        return n.d(e, "a", e), e
    }, n.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e)
    }, n.p = "", n(n.s = 2)
}([, , function (t, e, n) {
    n(3)
}, function (t, e) {
    layui.define(["form", "layer", "table", "common", "treeTable", "dropdown"], (function (t) {
        "use strict";
        var e, n, i, a, r, o = layui.form, l = layui.table, c = layui.layer, u = layui.common, d = layui.treeTable,
            f = layui.dropdown, s = layui.$, p = 0, b = 0, j = "",h = {
                tableIns: function (t, o, d=null, f="", param=!1) {
                    n = o, i = d, f && "" != f || (f = cUrl + "/list");
                    var m = s("#param").val();
                    if (m && (m = JSON.parse(m), s.isArray(m))) for (var y in m) f.indexOf("?") >= 0 ? f += "&" + m[y] : f += "?" + m[y];
                    t.forEach(k=>{
                        if(k.field == "custName" &&  k.templet == null){ //如果有客户号 ,且没有添加跳转链接
                            k.templet = function(w){
                                if(w.custNo == null)return w.custName;
                                return '<a class="layui-table-link" ew-href=/custwithin/custcodetail?custNo='+w.custNo+' lay-event="link">' + w.custName + '</a>'}
                        }else
                        //需要进行千分位转换的,请添加        AmountFormat:true 属性
                        if("undefined" != typeof (k.AmountFormat) && k.AmountFormat){
                            let fieldName = k.field;
                            k.templet = function(w){
                                let value = w[fieldName];
                                return value == 0 ? 0: value.toFixed(2).replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,');
                            }
                        }
                    });
                    return e = l.render({
                        elem: "#" + n,
                        url: f,
                        method: "post",
                        cellMinWidth: 150,
                        page: {
                            layout: ["refresh", "prev", "page", "next", "skip", "count", "limit"],
                            curr: 1,
                            groups: 10,
                            first: "首页",
                            last: "尾页"
                        },
                        height: "full-100",
                        limit: 20,
                        limits: [20, 40, 60, 80, 100, 150, 200, 500, 1000],
                        even: !0,
                        cols: [t],
                        loading: !0,
                        done: function (t, e, n) {
                            let $wid = s(".layui-table-header").width();
                            let $cod = s(".layui-table-header table tr").width();
                            let css = '',$v = 0;
                            if($cod < $wid){
                                let $cha = ($wid-10) - $cod;//-10是因为表头那里有10px的空余
                                 $v = $cha / s(".layui-table-header table tr").children().length; //计算多余的空隙,下面分配这些空隙
                            }
                            s(".layui-table-header table tr:eq(0) th .layui-table-cell").each(function (k,v) {
                                let $css = s(this).get(0).className.split(" ")[1]; //获取它的classs属性
                                let $b =s(this).get(0).scrollWidth; //标题的宽度/自定义的宽度
                                let $c = 0; //
                                if(s(".layui-table-main tbody tr").length > 0 ) $c = s(".layui-table-main tbody  tr:eq(0) td ."+$css).get(0).scrollWidth;//内容的宽度document.getElementsByClassName($css)[1].scrollWidth;
                                if($b > 400)$b = $c;
                                if($b > $c || $c > 400)$c = $b;//如果标题宽度 > 内容宽度 就用标题宽度 (可能有标题宽度 > 内容的)
                                let $cz =$c -  s(this).width();//已经扩大的
                                if($v - $cz >0 ){
                                    $c  +=  ($v - $cz);//把空隙补充上
                                }
                                css += "."+$css+"{ width:"+($c+1)+"px;}"; //拼接自定义的样式
                        });
                            if(css != '') s('.layui-table-view:eq(0) style').html(css);
                            //以上都是让设置table的样式
                            if (r) {
                                var i = s(".layui-table-body").find("table").find("tbody");
                                i.children("tr").on("dblclick", (function () {
                                    var e = i.find(".layui-table-hover").data("index"), n = t.data[e];
                                    u.edit(a, n.id, p, b)
                                }))
                            }
                           if(param.cursor){//提醒类的才需要 cUrl.indexOf("warn") > -1
                               h.cursorMoveShowTip();
                           }
                        }
                    }), l.on("toolbar(" + n + ")", (function (t) {
                        var e = l.checkStatus(t.config.id);
                        switch (t.event) {
                            case"getCheckData":
                                var n = e.data;
                                c.alert(JSON.stringify(n));
                                break;
                            case"getCheckLength":
                                n = e.data;
                                c.msg("选中了：" + n.length + " 个");
                                break;
                            case"isAll":
                                c.msg(e.isAll ? "全选" : "未全选")
                        }
                    })), l.on("tool(" + n + ")", (function (t) {

                        var e = t.data, n = t.event;
                        "edit" === n ? u.edit(a, e.id, p, b,[],{url:j}) : "detail" === n ? u.detail(a, e.id, p, b) : "del" === n ? u.delete(e.id, (function (e, n) {
                            n && t.del()
                        })) : "cache" === n ? u.cache(e.id) : "copy" === n ? u.copy(a, e.id, p, b) : "interView" === n ? u.interView(a, e, p, b,s(this).attr("data-param")) : i && i(n, e,t)
                    })), l.on("checkbox(" + n + ")", (function (t) {
                        if(t.type=="one")t.checked?s(t.tr).css("background-color","rgba(60, 141, 188, 0.51)"):s(t.tr).css("background-color","");//选中变色
                        if(t.type=="all")s("div[lay-id="+n+"] .layui-table tr").css("background-color","");
                    })), l.on("edit(" + n + ")", (function (t) {
                        var e = t.value, n = t.data, i = t.field, a = {};
                        a.id = n.id, a[i] = e;
                        //如果是排序字段的话  必须输入数字才行,且输入错误的话显示之前的值
                        if((i == "sort") && !new RegExp("^[0-9]*$").test(e)) {
                            let oldtext = s(t.tr.selector+' td[data-field="'+t.field+'"] div').text();
                            s(t.tr.selector+' td[data-field="'+t.field+'"] .layui-input').val(oldtext);
                            return  c.msg("请输入数字!");
                        };
                        if(i == "sort")a[i] = (a[i] == "" ?-1:a[i]);//如果设置排序字段为空值的话,设置为-1,后台处理
                        var r = JSON.stringify(a), o = JSON.parse(r), l = cUrl + "/update";
                        u.ajaxPost(l, o, (function (t, e) {
                        }), "更新中...")
                    })), l.on("row(" + n + ")", (function (t) {
                        t.tr.addClass("layui-table-click").siblings().removeClass("layui-table-click");
                    })), h && l.on("sort(" + n + ")", (function (t) {
                        l.reload(n, {initSort: t, where: {field: t.field, order: t.type}})
                    })), this
                }, treetable: function (t=[], e, i=!0, r=0, o="", l=null, f="") {
                    n = e, f || (f = cUrl + "/list");
                    var h = d.render({
                        elem: "#" + e,
                        url: f,
                        method: "POST",
                        height: "full-50",
                        cellMinWidth: 80,
                        tree: {iconIndex: 1, idName: "id", pidName: o || "pid", isPidData: !0},
                        cols: [t],
                        done: function (t, e, n) {
                            c.closeAll("loading")
                        },
                        style: "margin-top:0;"
                    });
                    d.on("tool(" + e + ")", (function (t) {
                        var e = t.data, n = t.event, i = e.id;
                        "add" === n ? u.edit(a, 0, p, b, ["pid=" + i]) : "edit" === n ? u.edit(a, i, p, b) : "del" === n ? u.delete(i, (function (e, n) {
                            n && t.del()
                        })) : l && l(n, i, 0)
                    })), s("#collapse").on("click", (function () {
                        return h.foldAll(), !1
                    })), s("#expand").on("click", (function () {
                        return h.expandAll(), !1
                    })), s("#refresh").on("click", (function () {
                        return h.refresh(), !1
                    })), s("#search").click((function () {
                        var t = s("#keywords").val() || s(".layui-this").first().text();
                        return t ? h.filterData(t) : h.clearFilter(), !1
                    }))
                }, setWin: function (t, e=0, n=0, m = {}) {
                    if(m.url) j = m.url;
                    return a = t, p = e, b = n, this
                }, setDbclick: function (t) {
                    return r = t || !0, this
                }, searchForm: function (t, e) {
                    o.on("submit(" + t + ")", (function (t) {
                        return u.searchForm(l, t, e), !1
                    }))
                }, getCheckData: function (t) {
                    return t || (t = n), l.checkStatus(t).data
                }, initDate: function (t, e=null) {
                    u.initDate(t, (function (t, n) {
                        e && e(t, n)
                    }))
                }, showWin: function (t, e, n=0, i=0, a=[], r=2, o=[], l=null) {
                    u.showWin(t, e, n, i, a, r, o, (function (t, e) {
                        l && l(t, e)
                    }))
                }, showInterViewWin: function (viewSource,data,l=null) {
                    //有三个要点满足: 客户号:custNo  id       归属机构:belongOrgNo  如果属性名称不一致的话,就不能使用该方法
                    u.showWin("添加客户访谈记录", "/custinterviewrecord/other_edit?custNo="+data.custNo+"&viewSource="+viewSource,1100,0,  ["sourceWarnId="+data.id,"orgNo="+data.belongOrgNo],2,[],(function (t, e) {
                        l && l(t, e)
                    }));
                }, ajaxPost: function (t, e, n=null, i="处理中...") {
                    u.ajaxPost(t, e, n, i)
                }, ajaxGet: function (t, e, n=null, i="处理中...") {
                    u.ajaxGet(t, e, n, i)
                }, formSwitch: function (t, e="", n=null) {
                    u.formSwitch(t, e, (function (t, e) {
                        n && n(t, e)
                    }))
                }, uploadFile: function (t, e=null, n="", i="xls|xlsx", a=10240, r={}) {
                    u.uploadFile(t, (function (t, n) {
                        e && e(t, n)
                    }), n, i, a, r)
                }, dropdownClick: function (t, e=null) {
                    f.onFilter(t, (function (t) {
                        e && e(t)
                    }))
                }, popMenu: function (t, e, n=null) {
                    var i = e.split("|");
                    f.suite("[lay-filter='" + t + "']", {
                        template: "#" + t, success: function (e) {
                            s.each(i, (function (i, a) {
                                e.find("." + a).click((function () {
                                    n && n(a), f.hide("[lay-filter='" + t + "']")
                                }))
                            }))
                        }
                    })
                }, tableHandler:function (option) {
                    var defaults = {
                        align:'center',
                        field:"",
                        otherField:"",
                    };
                    var options = s.extend(defaults,option,true);
                    options.field = null == options.field ? null==options.otherField ? "":options.otherField :options.field;
                    return "<div style = \"text-align:"+options.align+"\">"+options.field+"</div>";
                }, isEmpty: function (e) {
                    return null == e || undefined === e || ""===e;
                } ,uploadExcel:function (option) {
                    return u.uploadExcel(option);
                } , toast: function (option) { //iziToast弹窗
                    var defaults = {
                        message:"",
                        title:"",
                        timeout:3000,
                        onClosing:function () {
                        }
                    };
                    var options = s.extend(defaults,option,true);
                    return {
                       info: function(){
                           iziToast.info({
                               icon:"layui-icon layui-icon-about",
                               titleLineHeight:30,
                               timeout:options.timeout,
                               layout:2,
                               title: options.title,
                               transitionIn:'fadeInLeft',
                               transitionOut:"fadeOutRight",
                               maxWidth:"300px",
                               message: options.message,
                               onClosing:function () {
                                   options.onClosing();
                               },
                               onOpening:function () {
                               }
                           })
                       }, success: function () {
                            iziToast.success({
                                icon:"layui-icon layui-icon-about",
                                titleLineHeight:30,
                                timeout:options.timeout,
                                layout:2,
                                title: options.title,
                                message: options.message,
                                onClosing:function () {
                                    options.onClosing();
                                },
                            })
                        }
                        , custom1: function () {
                            iziToast.show({
                                class:options.class,
                                theme: 'dark',
                                backgroundColor:"rgba(0,0,0,0.6)",
                                progressBarColor:"#59cd6f",
                                icon:"layui-icon layui-icon-about",
                                titleLineHeight:20,
                                titleSize:12,
                                layout:2,
                                timeout:options.timeout,
                                title: options.title,
                                transitionIn:'fadeInLeft',
                                transitionOut:"fadeOutRight",
                                messageSize:10,
                                maxWidth:"385px",
                                message: options.message,
                                onClosing:function () {
                                    options.onClosing();
                                },
                            });
                        }
                    }
                },
                cursorMoveShowTip:function () {
                    let tps = s(".layui-table-body table tr").find("td[data-field='custName']");
                    let tipIndex = 0;
                    tps.on("mouseover", (function () {
                        let html = '',head = '',start = 0;
                        let td = s(this).nextAll("td");
                        let operW = s(".layui-table-fixed-r").width()-30;//左侧操作栏的宽度
                        let exclude =  ['status','acctStatRemark','remDays','toolbar','remark1','toolbar','ext1']; //排除字段
                        s.each(td ,function (i,v) {
                            let field = v.getAttribute("data-field");
                            //如果满足条件,说明该列已经被隐藏了 需要开始显示了
                            if(start > 0 && (exclude.filter(d=>d == field).length == 0)){
                                html += s(v).prop("outerHTML");
                                head += s(".layui-table-header table tr").find("th[data-field='"+field+"']").prop("outerHTML");
                                start ++;//标记
                            }
                            if(start == 0 && (parseInt(s(this).offset().left + operW+ s(this).width())) > s(".layui-table-header").width()){
                                html += s(v).prop("outerHTML");
                                head += s(".layui-table-header table tr").find("th[data-field='"+field+"']").prop("outerHTML");
                                start ++;//标记
                            }
                        });
                        let width =( 70 * start ) > 800 ? 800:( 70 * start);
                        tipIndex = c.tips("<table border='1' style='color: gray;border: 1px solid #e6e6e6;width:"+width+"px;max-width: 800px;table-layout: fixed;word-break: break-all;height: 50px' >" +
                            "<thead>"+head+"</thead>"+
                            "<tbody>"+html+"</tbody>"+
                            "</table>",s(this).next(),{tips:[3,'#fff'],area:[width,'auto'],time:0});

                        s(".layui-layer-tips table div").removeClass();
                        s(".layui-layer-tips .layui-layer-content").css('height',"auto")
                    }));
                    s(document.body).click(function () {
                        layer.close(tipIndex);
                    })
                }
            };
        u.verify(), o.on("submit(submitForm)", (function (t) {
            return u.submitForm(t.field, null, (function (t, e) {
                console.log("保存成功回调")
            })), !1
        })), o.on("submit(searchForm)", (function (t) {
            return u.searchForm(l, t), !1
        })), s(".btnOption").click((function () {
            var t = s(this).attr("data-param");
            null != t && (t = JSON.parse(t));
            var i = h.getCheckData(n), r = s(this).attr("lay-event");
            switch (r) {
                case"reset":
                    //重置查询条件
                    var sform = document.getElementById("queryForm");
                    if(sform.elements.length){
                        for(var i=0;i<sform.length;i++) {
                            //遍历每个表单元素，置空
                            sform[i].value = "";
                        }
                    }
                    break;
                case"add":
                    u.edit(a, 0, p, b, t,{url:j});
                    break;
                case"batchDrop":
                    (o = {title: "批量删除"}).url = cUrl + "/batchDelete", o.data = i, o.confirm = !0, o.type = "GET", u.batchFunc(o, (function () {
                        e.reload()
                    }));
                    break;
                case"batchCache":
                    (o = {title: "批量重置缓存"}).url = cUrl + "/batchCache", o.data = i, o.confirm = !0, o.type = "GET", u.batchFunc(o, (function () {
                        e.reload()
                    }));
                    break;
                case"batchEnable":
                    (o = {title: "批量启用状态"}).url = cUrl + "/batchStatus", o.param = t, o.data = i, o.form = "submitForm", o.confirm = !0, o.show_tips = "处理中...", o.type = "POST", u.batchFunc(o, (function () {
                        e.reload()
                    }));
                    break;
                case"batchRead":
                    if(i == 0) return  c.msg("请选择记录!", {icon: 5}),!1;
                    let data = i.filter(data => data.status == 0);
                   // let data = i;
                    if(data == 0) {
                        c.msg("该条记录已经被处理过,请重新选择", {icon: 5}) ;
                        return  false;
                    }
                    let params = [];
                    if(A.indexOf("weaindex") > -1 ){ //说明是财富客户的提醒 - 更简便的做法
                        params.push("type=wea");
                    }else if(A.indexOf("weahistory") > -1 ){//财富客户的历史数据的
                        params.push("type=weahistory");
                    }else if(A.indexOf("history") > -1 ){//历史数据的
                        params.push("type=history");
                    }
                    t  = params;
                    (o = {title: "批量阅读"}).url= cUrl + "/batchStatus", o.param = t, o.data = data, o.form = "submitForm", o.confirm = !0, o.show_tips = "处理中...", o.type = "POST", u.batchFunc(o, (function () {
                        e.reload()
                    }));
                    break;
                case"batchDisable":
                    var o;
                    (o = {title: "批量禁用状态"}).url = cUrl + "/batchStatus", o.param = t, o.data = i, o.confirm = !0, o.show_tips = "处理中...", o.type = "POST", u.batchFunc(o, (function () {
                        e.reload()
                    }));
                    break;
                case"export":
                    c.msg("导出"), location.href = cUrl + "/btn" + r.substring(0, 1).toUpperCase() + r.substring(1);
                    break;
                case"history":
                    let url = cUrl +"/history";
                    //如果是财富客户的话 也要考虑到
                    if(A.indexOf("weaindex") > -1 ){ url = cUrl + "/weahistory"}
                    let historyThis = s(this);
                        var title ="历史数据", n = historyThis.data("window");
                        n = n ? r.strToWin(n) : top;
                        var o = historyThis.attr("ew-end");
                        try {
                            o = o ? new Function(o) : void 0
                        } catch (e) {
                            console.error(e)
                        }
                        n.layui && n.layui.index ? n.layui.index.openTab({
                            title: title || "",
                            url: url,
                            end: o
                        }) : location.href = url
                    break;
                case"import":
                    u.uploadFile("import", (function (t, e) {
                    }))
            }
        })), window.formClose = function () {
            var t = parent.layer.getFrameIndex(window.name);
            parent.layer.close(t)
        }, t("func", h)
        //自定义按钮事件
        s("body").on("click","a[lay-event='custom-win']",function () {
            var $s = s(this);
            var eventFilter = s(this).attr("event-filter");
            var default_event_options = function (event_type) {
                var default_common_options = {
                    weight:400,
                    height:400,
                    title:"",
                };
                switch (event_type){
                    case "add":
                        return default_common_options;
                    case "edit":
                        return s.extend(default_common_options,{"id":$s.attr("data-id")},true);
                    case "read":
                        return s.extend(default_common_options,{"id":$s.attr("data-id")},true);
                }
            };
            var config = s.extend(default_event_options(eventFilter),JSON.parse(s(this).attr("event-config")),true);
            var url;
            if(config.url.indexOf("?") > 0){
                url = config.url + (eventFilter=="edit" || eventFilter=="read"  ? "&id=" + config.id : "");
            }else {
                url = config.url + (eventFilter=="edit" || eventFilter=="read"  ? "?id=" + config.id : "");
            }
            layui.func.showWin(config.title, url, config.weight, config.height);
        })
    }));
}]);