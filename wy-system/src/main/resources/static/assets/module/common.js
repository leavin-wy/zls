!function (n) {
    var e = {};

    function t(i) {
        if (e[i]) return e[i].exports;
        var r = e[i] = {i: i, l: !1, exports: {}};
        return n[i].call(r.exports, r, r.exports, t), r.l = !0, r.exports
    }

    t.m = n, t.c = e, t.d = function (n, e, i) {
        t.o(n, e) || Object.defineProperty(n, e, {enumerable: !0, get: i})
    }, t.r = function (n) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(n, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(n, "__esModule", {value: !0})
    }, t.t = function (n, e) {
        if (1 & e && (n = t(n)), 8 & e) return n;
        if (4 & e && "object" == typeof n && n && n.__esModule) return n;
        var i = Object.create(null);
        if (t.r(i), Object.defineProperty(i, "default", {
            enumerable: !0,
            value: n
        }), 2 & e && "string" != typeof n) for (var r in n) t.d(i, r, function (e) {
            return n[e]
        }.bind(null, r));
        return i
    }, t.n = function (n) {
        var e = n && n.__esModule ? function () {
            return n.default
        } : function () {
            return n
        };
        return t.d(e, "a", e), e
    }, t.o = function (n, e) {
        return Object.prototype.hasOwnProperty.call(n, e)
    }, t.p = "", t(t.s = 0)
}([function (n, e, t) {
    t(1)
}, function (n, e) {
    layui.define(["form", "layer", "laydate", "upload", "element", "base"], (function (n) {
        "use strict";

        /**
         * 字符串base64加密
         * @constructor
         */
        function Base64() {
            var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

            this.encode = function (input) {
                var output = "";
                var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
                var i = 0;
                input = _utf8_encode(input);
                while (i < input.length) {
                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);
                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;
                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }
                    output = output +
                        _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                        _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
                }
                return output;
            }

            // private method for UTF-8 encoding
            var _utf8_encode = function (string) {
                string = string.replace(/\r\n/g,"\n");
                var utftext = "";
                for (var n = 0; n < string.length; n++) {
                    var c = string.charCodeAt(n);
                    if (c < 128) {
                        utftext += String.fromCharCode(c);
                    } else if((c > 127) && (c < 2048)) {
                        utftext += String.fromCharCode((c >> 6) | 192);
                        utftext += String.fromCharCode((c & 63) | 128);
                    } else {
                        utftext += String.fromCharCode((c >> 12) | 224);
                        utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                        utftext += String.fromCharCode((c & 63) | 128);
                    }

                }
                return utftext;
            }
        }

        var e = layui.form, t = void 0 === parent.layer ? layui.layer : top.layer, i = layui.laydate, r = layui.upload,
            a = (layui.element, layui.base), o = layui.$, l = {
                edit: function (n, e = 0, t = 0, i = 0, r = [],s = {}) {
                    var o = e > 0 ? "修改" : "新增";
                    a.isEmpty(n) ? o += "内容" : o += n;
                    var u;
                    if (s.url) {
                        if (s.url.indexOf("?")) {
                            u = s.url + "&id=" + e;
                        }else{
                            u = s.url + "?id=" + e;
                        }
                    }else{
                        u = cUrl + "/edit?id=" + e
                    }
                    if (Array.isArray(r)) for (var c in r) u += "&" + r[c];
                    l.showWin(o, u, t, i, r)
                }, detail: function (n, e, t = 0, i = 0) {
                    var r = cUrl + "/detail?id=" + e;
                    l.showWin(n + "详情", r, t, i)
                }, interView: function (n, e, t = 0, i = 0,data) {
                    //访谈历史  data- 就是viewsource
                    let r ="/custinterviewrecord/history?custNo="+e.custNo+"&viewSource="+data;
                    l.showWin( "访谈历史", r, t, i)
                }, cache: function (n) {
                    var e = cUrl + "/cache";
                    l.ajaxPost(e, {id: n}, (function (n, e) {
                    }))
                }, copy: function (n, e, t = 0, i = 0) {
                    var r = cUrl + "/copy?id=" + e;
                    l.showWin(n + "复制", r, t, i)
                }, delete: function (n, e = null) {
                    t.confirm("您确定要删除吗？删除后将无法恢复！", {icon: 3, skin: "layer-ext-moon", btn: ["确认", "取消"]}, (function (i) {
                        var r = cUrl + "/delete/" + n;
                        console.log(r), l.ajaxGet(r, {}, (function (n, r) {
                            e && (t.close(i), e(n, r))
                        }), "正在删除。。。")
                    }))
                }, batchFunc: function (n, e = null) {
                    var i = n.url, r = n.title, a = (n.form, n.confirm || !1), o = n.show_tips || "处理中...",
                        u = n.data || [], c = n.param || [], s = n.type || "POST";
                    if (0 == u.length) return t.msg("请选择数据", {icon: 5}), !1;
                    var f = [];
                    for (var d in u) f.push(u[d].id);
                    var y = f.join(","), m = {};
                    if (m.ids = y, Array.isArray(c)) for (var d in c) {
                        var p = c[d].split("=");
                        m[p[0]] = p[1]

                    }
                    a ? t.confirm("您确定要【" + r + "】选中的数据吗？", {icon: 3, title: "提示信息"}, (function (n) {
                        "POST" == s ? l.ajaxPost(i, m, e, o) : l.ajaxGet(i + "/" + y, {}, e, o)
                    })) : "POST" == s ? l.ajaxPost(i, m, e, o) : l.ajaxGet(i + "/" + y, {}, e, o)
                }, verify: function () {
                    e.verify({
                        required: function (n, e) {
                            var t = o(e).data("title");
                            if (n.trim() == "")
                                return undefined === o(e).attr("placeholder") ? "必填项不能为空" : o(e).attr("placeholder");
                            if (t || (t = o(e).parents(".layui-inline").find(".layui-form-label").text()).trim().indexOf("：") >= 0 && (t = t.substring(0, t.Length - 1).trim()), !n)
                                return undefined === o(e).attr("placeholder") ? "必填项不能为空" : o(e).attr("placeholder");
                            //}, number: [/^[0-9]*$/, "请输入数字"]
                        } , number: function (n, e) {
                            //最大字符长度 add by wuyu
                            var max = e.getAttribute('lay-max');
                            //输入框名称
                            var title = e.getAttribute('title');
                            if(title==null)title='';
                            if(!max){
                                if(!new RegExp("^[+-]?[0-9]*$").test(n)){
                                    return "请输入数字";
                                }
                            }else {
                                if(!new RegExp("^[+-]?[0-9]{0,"+(max > 1 ? max : 10)+"}$").test(n)){
                                    return  (title ? "":title) +'最多只能'+max+'位数字';
                                }
                            }
                        }, username: function (n, e) {
                            return new RegExp("^[a-zA-Z0-9_一-龥\\s·]+$").test(n) ? /(^\_)|(\__)|(\_+$)/.test(n) ? "首尾不能出现下划线'_'" : void 0 : "不能含有特殊字符"
                        }, pass: [/^[\S]{6,12}$/, "密码必须6到12位，且不能出现空格"]
                        , clength: function (n,e) {
                            //最大字符长度 add by wuyu
                            var max = e.getAttribute('lay-max');
                            //输入框名称
                            var title = e.getAttribute('title');
                            if(title== null){
                                if ((title = o(e).parents(".layui-inline").find(".layui-form-label").text()).trim().indexOf("：") >= 0 && (title = title.split("：")[0]), !0)
                                    title = (undefined === o(e).attr("placeholder") ? "" : o(e).attr("placeholder").substring(3));
                            }
                            if(max == null) max = 20;
                            var i,sum;
                            sum = 0;
                            for(i=0;i<n.length;i++){
                                if ((n.charCodeAt(i)>=0) && (n.charCodeAt(i)<=255)){
                                    if(n.charCodeAt(i)>127||n.charCodeAt(i)==94){
                                        sum += 2;
                                    }else{
                                        sum += 1;
                                    }
                                }else{
                                    sum += 3;
                                }
                            }
                            if (sum > max) {
                                return  title+'最多只能'+max+'个字符或者'+Math.floor(max/3)+'个中文';
                            }
                        }, realNumber: [ /^[+-]?\d+(\.\d*)?$/,'请填入数值'],
                        realNumberOrNull: [ /(^$)|(^[+-]?\d+(\.\d*)?$)/,'请填入数值']
                        , ratio:[/^(\d?\d(\.\d*)?|100)$/,'请输入100以内的实数']
                        , dateRange: function(n,e){ //日期范围
                            var $relatedId = o("#"+e.getAttribute("related-id"));
                            var compareValue1 = e.getAttribute("compare-value");
                            var compareValue2 = $relatedId.attr("compare-value");
                            if(n && $relatedId){
                                var d1 = new Date(Date.parse(n.replace(/-/g,"\/")));
                                var d2 = new Date(Date.parse($relatedId.val().replace(/-/g,"\/")));
                                if(compareValue1 > compareValue2 && d1 < d2){
                                    return "日期范围不正确!";
                                }else if(compareValue1 < compareValue2 && d1 > d2){
                                    return "日期范围不正确!";
                                }
                            }
                        }
                        , custom: function(n,e){ //自定义
                            var func_verify_str = o(e).attr("custom-verify");
                            var func_verify = eval(func_verify_str);
                            return func_verify({data:n,elem:e});
                        }
                    })
                }, submitForm: function (n, e = null, k = null, r = !0) {
                    var u = [], c = [], s = n;
                    o("button").addClass("layui-btn-disabled");
                    o("button").attr('disabled', 'disabled');
                    if (o.each(s, (function (n, e) {
                        if (/\[|\]|【|】/g.test(n)) {
                            var t = n.match(/\[(.+?)\]/g);
                            e = n.match("\\[(.+?)\\]")[1];
                            var i = n.replace(t, "");
                            o.inArray(i, u) < 0 && u.push(i), c[i] || (c[i] = []), c[i].push(e)
                        }
                    })), o.each(u, (function (n, e) {
                        var t = [];
                        o.each(c[e], (function (n, i) {
                            t.push(i), delete s[e + "[" + i + "]"]
                        })), s[e] = t.join(",")
                    })), null == e) {
                        e = cUrl;
                        var f = o("form").attr("action");
                        a.isEmpty(f) ? null != n.id && (0 == n.id ? e += "/add" : n.id > 0 && (e += "/update")) : e = f
                    }
                    t.confirm("您确定提交数据吗？", {icon: 3, skin: "layer-ext-moon",title:'温馨提示', btn: ["确认", "取消"]}, (function (i) {
                        if(e == "/admin/add"){
                            console.info("-----" + s.password);
                            var base64 = new Base64();
                            s.password = base64.encode(s.password);
                            console.info("-----" + s.password);
                        }
                        l.ajaxPost(e, s, (function (n, e) {
                            if (e) return r && setTimeout((function () {
                                var options = o("button[lay-filter='submitForm']").attr("options");
                                if("underline"==options) options = "1|1";
                                var optionsArr = options.split("|");
                                if(optionsArr[0]==1){
                                    t.closeAll("iframe");
                                }
                                if(optionsArr[1]==1){
                                    parent.location.reload();
                                }
                                if(optionsArr[1]==0){
                                    /*if(o.isFunction(parent.reload_row)){
                                        parent.reload_row(s); // 如果想要不整个刷新页面,js里面要定义该方法的实现 s是数据
                                    }*/
                                    parent.reloadTable();//只刷新父页面的表格,js里面要定义该方法的实现
                                    parent.layer.closeAll("iframe");//关闭弹窗
                                }
                            }), 100), k && k(n, e), !1
                        }))
                    }));
                    o('button').removeClass("layui-btn-disabled");
                    o('button').removeAttr('disabled');
                }, searchForm: function (n, e, t = "tableList") {
                    n.reload(t, {page: {curr: 1}, where: e.field})
                }, initDate: function (n, e = null) {
                    if (Array.isArray(n)) for (var t in n) {
                        var r = n[t].split("|");
                        if (r[2]) var a = r[2].split(",");
                        var o = {};
                        if (o.elem = "#" + r[0], o.type = r[1], o.theme = "molv", o.range = "true" === r[3] || r[3], o.calendar = !0, o.show = !1, o.position = "absolute", o.trigger = "click", o.btns = ["clear", "now", "confirm"], o.mark = {
                            "0-06-25": "生日",
                            "0-12-31": "跨年"
                        }, o.ready = function (n) {
                        }, o.change = function (n, e, t) {
                        }, o.done = function (n, t, i) {
                            e && e(n, t)
                        }, a) {
                            var l = a[0];
                            if (l) {
                                var u = !isNaN(l);
                                o.min = u ? parseInt(l) : l
                            }
                            var c = a[1];
                            if (c) {
                                var s = !isNaN(c);
                                o.max = s ? parseInt(c) : c
                            }
                        }
                        i.render(o)
                    }
                }, showWin: function (n, e, t = 0, i = 0, r = [], a = 2, l = [], u = null) {
                    //设置默认宽高
                    var $body = o(window.top.document).find(".layui-layout-body:first-of-type .layui-body");
                    t = t == 0 ? $body.innerWidth() * 0.85:t;
                    i = i == 0 ? $body.innerHeight() * 0.85:i;
                    var c = layui.layer.open({
                        title: n,
                        type: a,
                        area: [t + "px", i + "px"],
                        content: e,
                        shadeClose: !0,
                        shade: .4,
                        skin: "layui-layer-admin",
                        success: function (n, e) {
                            if (Array.isArray(r)) for (var t in r) {
                                var i = r[t].split("=");
                                layui.layer.getChildFrame("body", e).find("#" + i[0]).val(i[1])

                            }
                            u && u(n, e)
                        },
                        end: function () {
                        },
                        cancel: function(){
                            //r为1时刷新父页面
                            if(r == 1){
                                location.reload();
                            }
                        }
                    });
                    0 == t && (layui.layer.full(c), o(window).on("resize", (function () {
                        layui.layer.full(c)
                    })))
                }, ajaxPost: function (n, e, i = null, r = "处理中,请稍后...") {
                    var a = null;
                    o.ajax({
                        type: "POST",
                        url: n,
                        data: JSON.stringify(e),
                        contentType: "application/json",
                        dataType: "json",
                        beforeSend: function () {
                            a = t.msg(r, {icon: 16, shade: .01, time: 0})
                        },
                        success: function (n) {
                            if (0 != n.code) return t.close(a),i(n,!1), t.msg(n.msg, {icon: 5}), !1;
                            t.msg(n.msg, {icon: 1, time: 500}, (function () {
                                t.close(a);
                            }));
                            i(n,0== n.code);
                        },
                        error: function (n) {
                            //t.close(a), t.msg("请求异常"), i && i(null, !1)
                            if(n.msg==null||t.msg==""){
                                console.log("8888888")
                                //t.close(a)
                                t.close(a), t.msg("请求异常"), i && i(null, !1)
                            }else{
                                console.log("9999999")
                                t.msg(n.msg, {icon: 1, time: 500}, (function () {
                                    t.close(a), i && i(n, !0)
                                }))
                            }

                        }
                    })
                }, ajaxGet: function (n, e, i = null, r = "处理中,请稍后...") {
                    var a = null;
                    o.ajax({
                        type: "GET",
                        url: n,
                        data: e,
                        contentType: "application/json",
                        dataType: "json",
                        beforeSend: function () {
                            a = t.msg(r, {icon: 16, shade: .01, time: 0})
                        },
                        success: function (n) {
                            if (0 != n.code) return t.close(a),i(n,!1), t.msg(n.msg, {icon: 5}), !1;
                            if(n.msg){
                                t.msg(n.msg, {icon: 1, time: 500}, (function () {
                                    t.close(a);
                                }));
                            }else {
                                t.close(a);
                            }
                            i(n,0== n.code);
                        },
                        error: function () {
                            t.msg("请求异常"), i && i(null, !1)
                        }
                    })
                }, formSwitch: function (n, t = "", i = null) {
                    e.on("switch(" + n + ")", (function (e) {
                        var r = this.checked ? "1" : "2";
                        a.isEmpty(t) && (t = cUrl + "/set" + n.substring(0, 1).toUpperCase() + n.substring(1));
                        var o = {};
                        o.id = this.value, o[n] = r;
                        var u = JSON.stringify(o);
                        JSON.parse(u);
                        l.ajaxPost(t, o, (function (n, e) {
                            i && i(n, e)
                        }))
                    }))
                }, uploadFile: function (n, e = null, i = "", o = "xls|xlsx", l = 10240, u = {}) {
                    a.isEmpty(i) && (i = cUrl + "/uploadFile"), r.render({
                        elem: "#" + n,
                        url: i,
                        auto: !1,
                        exts: o,
                        accept: "file",
                        size: l,
                        method: "post",
                        data: u,
                        before: function (n) {
                            t.msg("上传并处理中。。。", {icon: 16, shade: .01, time: 0})
                        },
                        done: function (n) {
                            return t.closeAll(), 0 == n.code ? t.alert(n.msg, {
                                title: "上传反馈",
                                skin: "layui-layer-molv",
                                closeBtn: 1,
                                anim: 0,
                                btn: ["确定", "取消"],
                                icon: 6,
                                yes: function () {
                                    e && e(n, !0)
                                },
                                btn2: function () {
                                }
                            }) : t.msg(n.msg, {icon: 5}), !1
                        },
                        error: function () {
                            return t.msg("数据请求异常")
                        }
                    })
                }, uploadExcel: function (option) {
                    var defaults = {
                        elem: '#uploadExcel',//绑定元素
                        url: cUrl+ '/uploadExcel',//上传接口
                        before: function () {
                        },
                        done: function (res) {
                        },
                        error: function () {

                        }
                    };
                    var options = o.extend(defaults,option,true);
                    r.render({
                        elem: options.elem
                        , accept: 'file'
                        , acceptMime: 'file/xlsx,file/xls'
                        , url: options.url
                        , before: function () {
                            t.msg("上传并处理中。。。", {icon: 16, shade: .01, time: 0});
                            options.before();
                        }
                        , done: function (res) {
                            console.info(res)
                            //上传完毕回调
                            t.closeAll();
                            if(res.code != '0') t.msg(res.msg);
                            else t.msg("上传成功！");
                            options.done(res);
                        }
                        , error: function () {
                            //请求异常回调
                            t.closeAll();
                            t.msg("请求异常！");
                            option.error();
                        }
                    });
                }
            };
        n("common", l)
    }))
}]);