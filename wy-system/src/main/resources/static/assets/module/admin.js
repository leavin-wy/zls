!function (e) {
    var t = {};

    function a(i) {
        if (t[i]) return t[i].exports;
        var n = t[i] = {i: i, l: !1, exports: {}};
        return e[i].call(n.exports, n, n.exports, a), n.l = !0, n.exports
    }

    a.m = e, a.c = t, a.d = function (e, t, i) {
        a.o(e, t) || Object.defineProperty(e, t, {enumerable: !0, get: i})
    }, a.r = function (e) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(e, "__esModule", {value: !0})
    }, a.t = function (e, t) {
        if (1 & t && (e = a(e)), 8 & t) return e;
        if (4 & t && "object" == typeof e && e && e.__esModule) return e;
        var i = Object.create(null);
        if (a.r(i), Object.defineProperty(i, "default", {
            enumerable: !0,
            value: e
        }), 2 & t && "string" != typeof e) for (var n in e) a.d(i, n, function (t) {
            return e[t]
        }.bind(null, n));
        return i
    }, a.n = function (e) {
        var t = e && e.__esModule ? function () {
            return e.default
        } : function () {
            return e
        };
        return a.d(t, "a", t), t
    }, a.o = function (e, t) {
        return Object.prototype.hasOwnProperty.call(e, t)
    }, a.p = "", a(a.s = 4)
}({
    4: function (e, t, a) {
        a(5)
    }, 5: function (e, t) {
        layui.define(["layer"], (function (e) {
            var t = layui.jquery, a = layui.layer, i = layui.cache, n = ".layui-layout-admin>.layui-body",
                o = n + ">.layui-tab", l = ".layui-layout-admin>.layui-side>.layui-side-scroll",
                s = ".layui-layout-admin>.layui-header", r = {
                    version: "3.1.8", layerData: {}, flexible: function (e) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.flexible(e);
                        var a = t(".layui-layout-admin"), i = a.hasClass("admin-nav-mini");
                        void 0 === e && (e = i), i === e && (window.sideFlexTimer && clearTimeout(window.sideFlexTimer), a.addClass("admin-side-flexible"), window.sideFlexTimer = setTimeout((function () {
                            a.removeClass("admin-side-flexible")
                        }), 600), e ? (r.hideTableScrollBar(), a.removeClass("admin-nav-mini")) : a.addClass("admin-nav-mini"), layui.event.call(this, "admin", "flexible({*})", {expand: e}))
                    }, activeNav: function (e) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.activeNav(e);
                        if (!e) return console.warn("active url is null");
                        t(l + ">.layui-nav .layui-nav-item .layui-nav-child dd.layui-this").removeClass("layui-this"), t(l + ">.layui-nav .layui-nav-item.layui-this").removeClass("layui-this");
                        var a = t(l + '>.layui-nav a[lay-href="' + e + '"]');
                        if (0 === a.length) return console.warn(e + " not found");
                        var i = t(".layui-layout-admin").hasClass("admin-nav-mini");
                        if ("_all" === t(l + ">.layui-nav").attr("lay-shrink")) {
                            var n = a.parent("dd").parents(".layui-nav-child");
                            i || t(l + ">.layui-nav .layui-nav-itemed>.layui-nav-child").not(n).css("display", "block").slideUp("fast", (function () {
                                t(this).css("display", "")
                            })), t(l + ">.layui-nav .layui-nav-itemed").not(n.parent()).removeClass("layui-nav-itemed")
                        }
                        a.parent().addClass("layui-this");
                        var o = a.parent("dd").parents(".layui-nav-child").parent();
                        if (!i) {
                            var c = o.not(".layui-nav-itemed").children(".layui-nav-child");
                            c.slideDown("fast", (function () {
                                if (t(this).is(c.last())) {
                                    c.css("display", "");
                                    var e = a.offset().top + a.outerHeight() + 30 - r.getPageHeight(),
                                        i = 115 - a.offset().top;
                                    e > 0 ? t(l).animate({scrollTop: t(l).scrollTop() + e}, 300) : i > 0 && t(l).animate({scrollTop: t(l).scrollTop() - i}, 300)
                                }
                            }))
                        }
                        o.addClass("layui-nav-itemed"), t('ul[lay-filter="admin-side-nav"]').addClass("layui-hide");
                        var d = a.parents(".layui-nav");
                        d.removeClass("layui-hide"), t(s + ">.layui-nav>.layui-nav-item").removeClass("layui-this"), t(s + '>.layui-nav>.layui-nav-item>a[nav-bind="' + d.attr("nav-id") + '"]').parent().addClass("layui-this")
                    }, popupRight: function (e) {
                        return e.anim = -1, e.offset = "r", e.move = !1, e.fixed = !0, void 0 === e.area && (e.area = "336px"), void 0 === e.title && (e.title = !1), void 0 === e.closeBtn && (e.closeBtn = !1), void 0 === e.shadeClose && (e.shadeClose = !0), void 0 === e.skin && (e.skin = "layui-anim layui-anim-rl layui-layer-adminRight"), r.open(e)
                    }, open: function (e) {
                        e.content && 2 === e.type && (e.url = void 0), !e.url || 2 !== e.type && void 0 !== e.type || (e.type = 1), void 0 === e.area && (e.area = 2 === e.type ? ["360px", "300px"] : "360px"), void 0 === e.offset && (e.offset = "70px"), void 0 === e.shade && (e.shade = .1), void 0 === e.fixed && (e.fixed = !1), void 0 === e.resize && (e.resize = !1), void 0 === e.skin && (e.skin = "layui-layer-admin");
                        var n = e.end;
                        if (e.end = function () {
                            a.closeAll("tips"), n && n()
                        }, e.url) {
                            var o = e.success;
                            e.success = function (a, i) {
                                t(a).data("tpl", e.tpl || ""), r.reloadLayer(i, e.url, o)
                            }
                        } else e.tpl && e.content && (e.content = r.util.tpl(e.content, e.data, i.tplOpen, i.tplClose));
                        var l = a.open(e);
                        return e.data && (r.layerData["d" + l] = e.data), l
                    }, getLayerData: function (e, t) {
                        if (void 0 === e) return void 0 === (e = parent.layer.getFrameIndex(window.name)) ? null : parent.layui.admin.getLayerData(parseInt(e), t);
                        if (isNaN(e) && (e = r.getLayerIndex(e)), void 0 !== e) {
                            var a = r.layerData["d" + e];
                            return t && a ? a[t] : a
                        }
                    }, putLayerData: function (e, t, a) {
                        if (void 0 === a) return void 0 === (a = parent.layer.getFrameIndex(window.name)) ? void 0 : parent.layui.admin.putLayerData(e, t, parseInt(a));
                        if (isNaN(a) && (a = r.getLayerIndex(a)), void 0 !== a) {
                            var i = r.getLayerData(a);
                            i || (i = {}), i[e] = t, r.layerData["d" + a] = i
                        }
                    }, reloadLayer: function (e, a, n) {
                        if ("function" == typeof a && (n = a, a = void 0), isNaN(e) && (e = r.getLayerIndex(e)), void 0 !== e) {
                            var o = t("#layui-layer" + e);
                            void 0 === a && (a = o.data("url")), a && (o.data("url", a), r.showLoading(o), r.ajax({
                                url: a,
                                dataType: "html",
                                success: function (a) {
                                    r.removeLoading(o, !1), "string" != typeof a && (a = JSON.stringify(a));
                                    var l = o.data("tpl");
                                    if (!0 === l || "true" === l) {
                                        var s = r.getLayerData(e) || {};
                                        s.layerIndex = e;
                                        var c = t("<div>" + a + "</div>"), d = {};
                                        for (var u in c.find("script,[tpl-ignore]").each((function (e) {
                                            var a = t(this);
                                            d["temp_" + e] = a[0].outerHTML, a.after("${temp_" + e + "}").remove()
                                        })), a = r.util.tpl(c.html(), s, i.tplOpen, i.tplClose), d) a = a.replace("${" + u + "}", d[u])
                                    }
                                    o.children(".layui-layer-content").html(a), r.renderTpl("#layui-layer" + e + " [ew-tpl]"), n && n(o[0], e)
                                }
                            }))
                        }
                    }, alert: function (e, t, i) {
                        return "function" == typeof t && (i = t, t = {}), void 0 === t.skin && (t.skin = "layui-layer-admin"), void 0 === t.shade && (t.shade = .1), a.alert(e, t, i)
                    }, confirm: function (e, t, i, n) {
                        return "function" == typeof t && (n = i, i = t, t = {}), void 0 === t.skin && (t.skin = "layui-layer-admin"), void 0 === t.shade && (t.shade = .1), a.confirm(e, t, i, n)
                    }, prompt: function (e, t) {
                        return "function" == typeof e && (t = e, e = {}), void 0 === e.skin && (e.skin = "layui-layer-admin layui-layer-prompt"), void 0 === e.shade && (e.shade = .1), a.prompt(e, t)
                    }, req: function (e, a, n, o, l) {
                        return "function" == typeof a && (l = o, o = n, n = a, a = {}), void 0 !== o && "string" != typeof o && (l = o, o = void 0), o || (o = "GET"), "string" == typeof a ? (l || (l = {}), l.contentType || (l.contentType = "application/json;charset=UTF-8")) : i.reqPutToPost && ("put" === o.toLowerCase() ? (o = "POST", a._method = "PUT") : "delete" === o.toLowerCase() && (o = "GET", a._method = "DELETE")), r.ajax(t.extend({
                            url: (i.baseServer || "") + e,
                            data: a,
                            type: o,
                            dataType: "json",
                            success: n
                        }, l))
                    }, ajax: function (e) {
                        var a = r.util.deepClone(e);
                        e.dataType || (e.dataType = "json"), e.headers || (e.headers = {});
                        var n = i.getAjaxHeaders(e.url);
                        if (n) for (var o = 0; o < n.length; o++) void 0 === e.headers[n[o].name] && (e.headers[n[o].name] = n[o].value);
                        var l = e.success;
                        return e.success = function (n, o, s) {
                            !1 !== i.ajaxSuccessBefore(r.parseJSON(n), e.url, {
                                param: a, reload: function (e) {
                                    r.ajax(t.extend(!0, a, e))
                                }, update: function (e) {
                                    n = e
                                }, xhr: s
                            }) ? l && l(n, o, s) : e.cancel && e.cancel()
                        }, e.error = function (t, a) {
                            e.success({code: t.status, msg: t.statusText}, a, t)
                        }, !layui.cache.version || i.apiNoCache && "json" === e.dataType.toLowerCase() || (-1 === e.url.indexOf("?") ? e.url += "?v=" : e.url += "&v=", !0 === layui.cache.version ? e.url += (new Date).getTime() : e.url += layui.cache.version), t.ajax(e)
                    }, parseJSON: function (e) {
                        if ("string" == typeof e) try {
                            return JSON.parse(e)
                        } catch (e) {
                        }
                        return e
                    }, showLoading: function (e, a, n, o) {
                        void 0 === e || "string" == typeof e || e instanceof t || (a = e.type, n = e.opacity, o = e.size, e = e.elem), void 0 === a && (a = i.defaultLoading || 1), void 0 === o && (o = "sm"), void 0 === e && (e = "body");
                        var l = ['<div class="ball-loader ' + o + '"><span></span><span></span><span></span><span></span></div>', '<div class="rubik-loader ' + o + '"></div>', '<div class="signal-loader ' + o + '"><span></span><span></span><span></span><span></span></div>', '<div class="layui-loader ' + o + '"><i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop"></i></div>'];
                        t(e).addClass("page-no-scroll"), t(e).scrollTop(0);
                        var s = t(e).children(".page-loading");
                        s.length <= 0 && (t(e).append('<div class="page-loading">' + l[a - 1] + "</div>"), s = t(e).children(".page-loading")), void 0 !== n && s.css("background-color", "rgba(255,255,255," + n + ")"), s.show()
                    }, removeLoading: function (e, a, i) {
                        void 0 === e && (e = "body"), void 0 === a && (a = !0);
                        var n = t(e).children(".page-loading");
                        i ? n.remove() : a ? n.fadeOut("fast") : n.hide(), t(e).removeClass("page-no-scroll")
                    }, putTempData: function (e, t, a) {
                        var n = a ? i.tableName : i.tableName + "_tempData";
                        null == t ? a ? layui.data(n, {key: e, remove: !0}) : layui.sessionData(n, {
                            key: e,
                            remove: !0
                        }) : a ? layui.data(n, {key: e, value: t}) : layui.sessionData(n, {key: e, value: t})
                    }, getTempData: function (e, t) {
                        "boolean" == typeof e && (t = e, e = void 0);
                        var a = t ? i.tableName : i.tableName + "_tempData", n = t ? layui.data(a) : layui.sessionData(a);
                        return e ? n ? n[e] : void 0 : n
                    }, rollPage: function (e) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.rollPage(e);
                        var a = t(o + ">.layui-tab-title"), i = a.scrollLeft();
                        if ("left" === e) a.animate({scrollLeft: i - 120}, 100); else if ("auto" === e) {
                            var n = 0;
                            a.children("li").each((function () {
                                if (t(this).hasClass("layui-this")) return !1;
                                n += t(this).outerWidth()
                            })), a.animate({scrollLeft: n - 120}, 100)
                        } else a.animate({scrollLeft: i + 120}, 100)
                    }, refresh: function (e, a) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.refresh(e);
                        var i;
                        if (e ? (!(i = t(o + '>.layui-tab-content>.layui-tab-item>.admin-iframe[lay-id="' + e + '"]')) || i.length <= 0) && (i = t(n + ">.admin-iframe")) : (!(i = t(o + ">.layui-tab-content>.layui-tab-item.layui-show>.admin-iframe")) || i.length <= 0) && (i = t(n + ">div>.admin-iframe")), !i || !i[0]) return console.warn(e + " is not found");
                        try {
                            a && i[0].contentWindow.refreshTab ? i[0].contentWindow.refreshTab() : (r.showLoading({
                                elem: i.parent(),
                                size: ""
                            }), i[0].contentWindow.location.reload())
                        } catch (e) {
                            console.warn(e), i.attr("src", i.attr("src"))
                        }
                    }, closeThisTabs: function (e) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.closeThisTabs(e);
                        r.closeTabOperNav();
                        var i = t(o + ">.layui-tab-title");
                        if (e) {
                            if (e === i.find("li").first().attr("lay-id")) return a.msg("主页不能关闭", {icon: 2});
                            i.find('li[lay-id="' + e + '"]').find(".layui-tab-close").trigger("click")
                        } else {
                            if (i.find("li").first().hasClass("layui-this")) return a.msg("主页不能关闭", {icon: 2});
                            i.find("li.layui-this").find(".layui-tab-close").trigger("click")
                        }
                    }, closeOtherTabs: function (e) {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.closeOtherTabs(e);
                        e ? t(o + ">.layui-tab-title li:gt(0)").each((function () {
                            e !== t(this).attr("lay-id") && t(this).find(".layui-tab-close").trigger("click")
                        })) : t(o + ">.layui-tab-title li:gt(0):not(.layui-this)").find(".layui-tab-close").trigger("click"), r.closeTabOperNav()
                    }, closeAllTabs: function () {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.closeAllTabs();
                        t(o + ">.layui-tab-title li:gt(0)").find(".layui-tab-close").trigger("click"), t(o + ">.layui-tab-title li:eq(0)").trigger("click"), r.closeTabOperNav()
                    }, closeTabOperNav: function () {
                        if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.closeTabOperNav();
                        t(".layui-icon-down .layui-nav .layui-nav-child").removeClass("layui-show")
                    }, changeTheme: function (e, t, a, i) {
                        if (a || r.putSetting("defaultTheme", e), t || (t = top), r.removeTheme(t), e) try {
                            var n = t.layui.jquery("body");
                            n.addClass(e), n.data("theme", e)
                        } catch (e) {
                        }
                        if (!i) for (var o = t.frames, l = 0; l < o.length; l++) r.changeTheme(e, o[l], !0, !1)
                    }, removeTheme: function (e) {
                        e || (e = window);
                        try {
                            var t = e.layui.jquery("body"), a = t.data("theme");
                            a && t.removeClass(a), t.removeData("theme")
                        } catch (e) {
                        }
                    }, closeThisDialog: function () {
                        return r.closeDialog()
                    }, closeDialog: function (e) {
                        //备注一下:此处的判断是用于积分台账的积分消费页面,虽然是新页面,但是有关闭按钮,所以加了此处判断,关闭tab
                        if("undefined" == typeof ( r.getLayerIndex(e))){r.closeThisTabs(e)};
                        e ? a.close(r.getLayerIndex(e)) : parent.layer.close(parent.layer.getFrameIndex(window.name))
                    }, getLayerIndex: function (e) {
                        if (!e) return parent.layer.getFrameIndex(window.name);
                        var a = t(e).parents(".layui-layer").first().attr("id");
                        return a && a.length >= 11 ? a.substring(11) : void 0
                    }, iframeAuto: function () {
                        return parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name))
                    }, getPageHeight: function () {
                        return document.documentElement.clientHeight || document.body.clientHeight
                    }, getPageWidth: function () {
                        return document.documentElement.clientWidth || document.body.clientWidth
                    }, modelForm: function (e, a, i) {
                        var n = t(e);
                        n.addClass("layui-form"), i && n.attr("lay-filter", i);
                        var o = n.find(".layui-layer-btn .layui-layer-btn0");
                        o.attr("lay-submit", ""), o.attr("lay-filter", a)
                    }, btnLoading: function (e, a, i) {
                        void 0 !== a && "boolean" == typeof a && (i = a, a = void 0), void 0 === a && (a = "&nbsp;加载中"), void 0 === i && (i = !0);
                        var n = t(e);
                        i ? (n.addClass("ew-btn-loading"), n.prepend('<span class="ew-btn-loading-text"><i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop"></i>' + a + "</span>"), n.attr("disabled", "disabled").prop("disabled", !0)) : (n.removeClass("ew-btn-loading"), n.children(".ew-btn-loading-text").remove(), n.removeProp("disabled").removeAttr("disabled"))
                    }, openSideAutoExpand: function () {
                        var e = t(".layui-layout-admin>.layui-side");
                        e.off("mouseenter.openSideAutoExpand").on("mouseenter.openSideAutoExpand", (function () {
                            t(this).parent().hasClass("admin-nav-mini") && (r.flexible(!0), t(this).addClass("side-mini-hover"))
                        })), e.off("mouseleave.openSideAutoExpand").on("mouseleave.openSideAutoExpand", (function () {
                            t(this).hasClass("side-mini-hover") && (r.flexible(!1), t(this).removeClass("side-mini-hover"))
                        }))
                    }, openCellAutoExpand: function () {
                        var e = t("body");
                        e.off("mouseenter.openCellAutoExpand").on("mouseenter.openCellAutoExpand", ".layui-table-view td", (function () {
                            t(this).find(".layui-table-grid-down").trigger("click")
                        })), e.off("mouseleave.openCellAutoExpand").on("mouseleave.openCellAutoExpand", ".layui-table-tips>.layui-layer-content", (function () {
                            t(".layui-table-tips-c").trigger("click")
                        }))
                    }, parseLayerOption: function (e) {
                        for (var a in e) e.hasOwnProperty(a) && e[a] && -1 !== e[a].toString().indexOf(",") && (e[a] = e[a].toString().split(","));
                        var i = {success: "layero,index", cancel: "index,layero", end: "", full: "", min: "", restore: ""};
                        for (var n in i) if (i.hasOwnProperty(n) && e[n]) try {
                            /^[a-zA-Z_]+[a-zA-Z0-9_]+$/.test(e[n]) && (e[n] += "()"), e[n] = new Function(i[n], e[n])
                        } catch (t) {
                            e[n] = void 0
                        }
                        return e.content && "string" == typeof e.content && 0 === e.content.indexOf("#") && (t(e.content).is("script") ? e.content = t(e.content).html() : e.content = t(e.content)), void 0 === e.type && void 0 === e.url && (e.type = 2), e
                    }, strToWin: function (e) {
                        var t = window;
                        if (!e) return t;
                        for (var a = e.split("."), i = 0; i < a.length; i++) t = t[a[i]];
                        return t
                    }, hideTableScrollBar: function (e) {
                        if (!(r.getPageWidth() <= 768)) {
                            if (!e) {
                                var a = t(o + ">.layui-tab-content>.layui-tab-item.layui-show>.admin-iframe");
                                a.length <= 0 && (a = t(n + ">div>.admin-iframe")), a.length > 0 && (e = a[0].contentWindow)
                            }
                            try {
                                window.hsbTimer && clearTimeout(window.hsbTimer), e.layui.jquery(".layui-table-body.layui-table-main").addClass("no-scrollbar"), window.hsbTimer = setTimeout((function () {
                                    e.layui.jquery(".layui-table-body.layui-table-main").removeClass("no-scrollbar")
                                }), 800)
                            } catch (e) {
                            }
                        }
                    }, isTop: function () {
                        return t(n).length > 0
                    }
                };
            r.events = {
                flexible: function () {
                    r.strToWin(t(this).data("window")).layui.admin.flexible()
                }, refresh: function () {
                    r.strToWin(t(this).data("window")).layui.admin.refresh()
                }, back: function () {
                    r.strToWin(t(this).data("window")).history.back()
                }, theme: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.popupRight(t.extend({
                        id: "layer-theme",
                        url: e.url || "/static/assets/libs/templets/tpl-theme.html"
                    }, r.parseLayerOption(e)))
                }, note: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.popupRight(t.extend({
                        id: "layer-note",
                        url: e.url || "/static/assets/libs/templets/tpl-note.html"
                    }, r.parseLayerOption(e)))
                }, message: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.popupRight(t.extend({
                        id: "layer-notice",
                        url: e.url || "/static/assets/libs/templets/tpl-message.html"
                    }, r.parseLayerOption(e)))
                }, psw: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.open(t.extend({
                        id: "layer-psw",
                        title: "修改密码",
                        shade: 0,
                        url: e.url || "/static/assets/libs/templets/tpl-password.html"
                    }, r.parseLayerOption(e)))
                }, logout: function () {
                    var e = r.util.deepClone(t(this).data());

                    function n() {
                        if (e.ajax) {
                            var t = a.load(2);
                            r.req(e.ajax, (function (n) {
                                if (a.close(t), e.parseData) try {
                                    n = new Function("res", e.parseData)(n)
                                } catch (e) {
                                    console.error(e)
                                }
                                n.code == (e.code || 0) ? (i.removeToken && i.removeToken(), location.replace(e.url || "/")) : a.msg(n.msg, {icon: 2})
                            }), e.method || "delete")
                        } else i.removeToken && i.removeToken(), location.replace(e.url || "/")
                    }

                    if (r.unlockScreen(), !1 === e.confirm || "false" === e.confirm) return n();
                    r.strToWin(e.window).layui.layer.confirm(e.content || "确定要退出登录吗？", t.extend({
                        title: "温馨提示",
                        skin: "layui-layer-admin",
                        shade: .1
                    }, r.parseLayerOption(e)), (function () {
                        n()
                    }))
                }, open: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.open(r.parseLayerOption(e))
                }, popupRight: function () {
                    var e = r.util.deepClone(t(this).data());
                    r.strToWin(e.window).layui.admin.popupRight(r.parseLayerOption(e))
                }, fullScreen: function () {
                    var e = "layui-icon-screen-full", a = "layui-icon-screen-restore", i = t(this).find("i");
                    if (document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || !1) {
                        var n = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
                        if (n) n.call(document); else if (window.ActiveXObject) {
                            var o = new ActiveXObject("WScript.Shell");
                            o && o.SendKeys("{F11}")
                        }
                        i.addClass(e).removeClass(a)
                    } else {
                        var l = document.documentElement,
                            s = l.requestFullscreen || l.webkitRequestFullscreen || l.mozRequestFullScreen || l.msRequestFullscreen;
                        if (s) s.call(l); else if (window.ActiveXObject) {
                            var r = new ActiveXObject("WScript.Shell");
                            r && r.SendKeys("{F11}")
                        }
                        i.addClass(a).removeClass(e)
                    }
                }, leftPage: function () {
                    r.strToWin(t(this).data("window")).layui.admin.rollPage("left")
                }, rightPage: function () {
                    r.strToWin(t(this).data("window")).layui.admin.rollPage()
                }, closeThisTabs: function () {
                    var e = t(this).data("url");
                    r.strToWin(t(this).data("window")).layui.admin.closeThisTabs(e)
                }, closeOtherTabs: function () {
                    r.strToWin(t(this).data("window")).layui.admin.closeOtherTabs()
                }, closeAllTabs: function () {
                    r.strToWin(t(this).data("window")).layui.admin.closeAllTabs()
                }, closeDialog: function () {
                    t(this).parents(".layui-layer").length > 0 ? r.closeDialog(this) : r.closeDialog()
                }, closeIframeDialog: function () {
                    r.closeDialog()
                }, closePageDialog: function () {
                    r.closeDialog(this)
                }, lockScreen: function () {
                    r.strToWin(t(this).data("window")).layui.admin.lockScreen(t(this).data("url"))
                }
            }, r.chooseLocation = function (e) {
                var i = e.title, n = e.onSelect, o = e.needCity, l = e.center, s = e.defaultZoom, c = e.pointZoom,
                    d = e.keywords, u = e.pageSize, p = e.mapJsUrl;
                void 0 === i && (i = "选择位置"), void 0 === s && (s = 11), void 0 === c && (c = 17), void 0 === d && (d = ""), void 0 === u && (u = 30), void 0 === p && (p = "https://webapi.amap.com/maps?v=1.4.14&key=006d995d433058322319fa797f2876f5");
                var y, m = !1, f = function (e, a) {
                        AMap.service(["AMap.PlaceSearch"], (function () {
                            var i = new AMap.PlaceSearch({type: "", pageSize: u, pageIndex: 1}), n = [a, e];
                            i.searchNearBy(d, n, 1e3, (function (e, a) {
                                if ("complete" === e) {
                                    for (var i = a.poiList.pois, n = "", o = 0; o < i.length; o++) {
                                        var l = i[o];
                                        void 0 !== l.location && (n += '<div data-lng="' + l.location.lng + '" data-lat="' + l.location.lat + '" class="ew-map-select-search-list-item">', n += '     <div class="ew-map-select-search-list-item-title">' + l.name + "</div>", n += '     <div class="ew-map-select-search-list-item-address">' + l.address + "</div>", n += '     <div class="ew-map-select-search-list-item-icon-ok layui-hide"><i class="layui-icon layui-icon-ok-circle"></i></div>', n += "</div>")
                                    }
                                    t("#ew-map-select-pois").html(n)
                                }
                            }))
                        }))
                    }, v = function () {
                        var e = {resizeEnable: !0, zoom: s};
                        l && (e.center = l);
                        var i = new AMap.Map("ew-map-select-map", e);
                        i.on("complete", (function () {
                            var e = i.getCenter();
                            f(e.lat, e.lng)
                        })), i.on("moveend", (function () {
                            if (m) m = !1; else {
                                t("#ew-map-select-tips").addClass("layui-hide"), t("#ew-map-select-center-img").removeClass("bounceInDown"), setTimeout((function () {
                                    t("#ew-map-select-center-img").addClass("bounceInDown")
                                }));
                                var e = i.getCenter();
                                f(e.lat, e.lng)
                            }
                        })), t("#ew-map-select-pois").off("click").on("click", ".ew-map-select-search-list-item", (function () {
                            t("#ew-map-select-tips").addClass("layui-hide"), t("#ew-map-select-pois .ew-map-select-search-list-item-icon-ok").addClass("layui-hide"), t(this).find(".ew-map-select-search-list-item-icon-ok").removeClass("layui-hide"), t("#ew-map-select-center-img").removeClass("bounceInDown"), setTimeout((function () {
                                t("#ew-map-select-center-img").addClass("bounceInDown")
                            }));
                            var e = t(this).data("lng"), a = t(this).data("lat"),
                                n = t(this).find(".ew-map-select-search-list-item-title").text(),
                                o = t(this).find(".ew-map-select-search-list-item-address").text();
                            y = {name: n, address: o, lat: a, lng: e}, m = !0, i.setZoomAndCenter(c, [e, a])
                        })), t("#ew-map-select-btn-ok").click((function () {
                            if (void 0 === y) a.msg("请点击位置列表选择", {icon: 2, anim: 6}); else if (n) if (o) {
                                var e = a.load(2);
                                i.setCenter([y.lng, y.lat]), i.getCity((function (t) {
                                    a.close(e), y.city = t, r.closeDialog("#ew-map-select-btn-ok"), n(y)
                                }))
                            } else r.closeDialog("#ew-map-select-btn-ok"), n(y); else r.closeDialog("#ew-map-select-btn-ok")
                        }));
                        var d = t("#ew-map-select-input-search");
                        d.off("input").on("input", (function () {
                            var e = t(this).val(), a = t("#ew-map-select-tips");
                            e || (a.html(""), a.addClass("layui-hide")), AMap.plugin("AMap.Autocomplete", (function () {
                                new AMap.Autocomplete({city: "全国"}).search(e, (function (e, i) {
                                    if (i.tips) {
                                        for (var n = i.tips, o = "", l = 0; l < n.length; l++) {
                                            var s = n[l];
                                            void 0 !== s.location && (o += '<div data-lng="' + s.location.lng + '" data-lat="' + s.location.lat + '" class="ew-map-select-search-list-item">', o += '     <div class="ew-map-select-search-list-item-icon-search"><i class="layui-icon layui-icon-search"></i></div>', o += '     <div class="ew-map-select-search-list-item-title">' + s.name + "</div>", o += '     <div class="ew-map-select-search-list-item-address">' + s.address + "</div>", o += "</div>")
                                        }
                                        a.html(o), 0 === n.length ? t("#ew-map-select-tips").addClass("layui-hide") : t("#ew-map-select-tips").removeClass("layui-hide")
                                    } else a.html(""), a.addClass("layui-hide")
                                }))
                            }))
                        })), d.off("blur").on("blur", (function () {
                            var e = t(this).val(), a = t("#ew-map-select-tips");
                            e || (a.html(""), a.addClass("layui-hide"))
                        })), d.off("focus").on("focus", (function () {
                            t(this).val() && t("#ew-map-select-tips").removeClass("layui-hide")
                        })), t("#ew-map-select-tips").off("click").on("click", ".ew-map-select-search-list-item", (function () {
                            t("#ew-map-select-tips").addClass("layui-hide");
                            var e = t(this).data("lng"), a = t(this).data("lat");
                            y = void 0, i.setZoomAndCenter(c, [e, a])
                        }))
                    },
                    h = ['<div class="ew-map-select-tool" style="position: relative;">', '     搜索：<input id="ew-map-select-input-search" class="layui-input icon-search inline-block" style="width: 190px;" placeholder="输入关键字搜索" autocomplete="off" />', '     <button id="ew-map-select-btn-ok" class="layui-btn icon-btn pull-right" type="button"><i class="layui-icon">&#xe605;</i>确定</button>', '     <div id="ew-map-select-tips" class="ew-map-select-search-list layui-hide">', "     </div>", "</div>", '<div class="layui-row ew-map-select">', '     <div class="layui-col-sm7 ew-map-select-map-group" style="position: relative;">', '          <div id="ew-map-select-map"></div>', '          <i id="ew-map-select-center-img2" class="layui-icon layui-icon-add-1"></i>', '          <img id="ew-map-select-center-img" src="https://3gimg.qq.com/lightmap/components/locationPicker2/image/marker.png" alt=""/>', "     </div>", '     <div id="ew-map-select-pois" class="layui-col-sm5 ew-map-select-search-list">', "     </div>", "</div>"].join("");
                r.open({
                    id: "ew-map-select", type: 1, title: i, area: "750px", content: h, success: function (e, a) {
                        var i = t(e).children(".layui-layer-content");
                        i.css("overflow", "visible"), r.showLoading(i), void 0 === window.AMap ? t.getScript(p, (function () {
                            v(), r.removeLoading(i)
                        })) : (v(), r.removeLoading(i))
                    }
                })
            }, r.cropImg = function (e) {
                var i = "image/jpeg", n = e.aspectRatio, o = e.imgSrc, l = e.imgType, s = e.onCrop, c = e.limitSize,
                    d = e.acceptMime, u = e.exts, p = e.title;
                void 0 === n && (n = 1), void 0 === p && (p = "裁剪图片"), l && (i = l), layui.use(["Cropper", "upload"], (function () {
                    var e = layui.Cropper, l = layui.upload;
                    var y = ['<div class="layui-row">', '     <div class="layui-col-sm8" style="min-height: 9rem;">', '          <img id="ew-crop-img" src="', o || "", '" style="max-width:100%;" alt=""/>', "     </div>", '     <div class="layui-col-sm4 layui-hide-xs" style="padding: 15px;text-align: center;">', '          <div id="ew-crop-img-preview" style="width: 100%;height: 9rem;overflow: hidden;display: inline-block;border: 1px solid #dddddd;"></div>', "     </div>", "</div>", '<div class="text-center ew-crop-tool" style="padding: 15px 10px 5px 0;">', '     <div class="layui-btn-group" style="margin-bottom: 10px;margin-left: 10px;">', '          <button title="放大" data-method="zoom" data-option="0.1" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-add-1"></i></button>', '          <button title="缩小" data-method="zoom" data-option="-0.1" class="layui-btn icon-btn" type="button"><span style="display: inline-block;width: 12px;height: 2.5px;background: rgba(255, 255, 255, 0.9);vertical-align: middle;margin: 0 4px;"></span></button>', "     </div>", '     <div class="layui-btn-group layui-hide-xs" style="margin-bottom: 10px;">', '          <button title="向左旋转" data-method="rotate" data-option="-45" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-refresh-1" style="transform: rotateY(180deg) rotate(40deg);display: inline-block;"></i></button>', '          <button title="向右旋转" data-method="rotate" data-option="45" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-refresh-1" style="transform: rotate(30deg);display: inline-block;"></i></button>', "     </div>", '     <div class="layui-btn-group" style="margin-bottom: 10px;">', '          <button title="左移" data-method="move" data-option="-10" data-second-option="0" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-left"></i></button>', '          <button title="右移" data-method="move" data-option="10" data-second-option="0" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-right"></i></button>', '          <button title="上移" data-method="move" data-option="0" data-second-option="-10" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-up"></i></button>', '          <button title="下移" data-method="move" data-option="0" data-second-option="10" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-down"></i></button>', "     </div>", '     <div class="layui-btn-group" style="margin-bottom: 10px;">', '          <button title="左右翻转" data-method="scaleX" data-option="-1" class="layui-btn icon-btn" type="button" style="position: relative;width: 41px;"><i class="layui-icon layui-icon-triangle-r" style="position: absolute;left: 9px;top: 0;transform: rotateY(180deg);font-size: 16px;"></i><i class="layui-icon layui-icon-triangle-r" style="position: absolute; right: 3px; top: 0;font-size: 16px;"></i></button>', '          <button title="上下翻转" data-method="scaleY" data-option="-1" class="layui-btn icon-btn" type="button" style="position: relative;width: 41px;"><i class="layui-icon layui-icon-triangle-d" style="position: absolute;left: 11px;top: 6px;transform: rotateX(180deg);line-height: normal;font-size: 16px;"></i><i class="layui-icon layui-icon-triangle-d" style="position: absolute; left: 11px; top: 14px;line-height: normal;font-size: 16px;"></i></button>', "     </div>", '     <div class="layui-btn-group" style="margin-bottom: 10px;">', '          <button title="重新开始" data-method="reset" class="layui-btn icon-btn" type="button"><i class="layui-icon layui-icon-refresh"></i></button>', '          <button title="选择图片" id="ew-crop-img-upload" class="layui-btn icon-btn" type="button" style="border-radius: 0 2px 2px 0;"><i class="layui-icon layui-icon-upload-drag"></i></button>', "     </div>", '     <button data-method="getCroppedCanvas" data-option="{ &quot;maxWidth&quot;: 4096, &quot;maxHeight&quot;: 4096 }" class="layui-btn icon-btn" type="button" style="margin-left: 10px;margin-bottom: 10px;"><i class="layui-icon">&#xe605;</i>完成</button>', "</div>"].join("");
                    r.open({
                        title: p, area: "665px", type: 1, content: y, success: function (p, y) {
                            t(p).children(".layui-layer-content").css("overflow", "visible"), function p() {
                                var y, m = t("#ew-crop-img"), f = {
                                    elem: "#ew-crop-img-upload", auto: !1, drag: !1, choose: function (t) {
                                        t.preview((function (t, a, n) {
                                            i = a.type, m.attr("src", n), o && y ? (y.destroy(), y = new e(m[0], v)) : (o = n, p())
                                        }))
                                    }
                                };
                                if (void 0 !== c && (f.size = c), void 0 !== d && (f.acceptMime = d), void 0 !== u && (f.exts = u), l.render(f), !o) return t("#ew-crop-img-upload").trigger("click");
                                var v = {aspectRatio: n, preview: "#ew-crop-img-preview"};
                                y = new e(m[0], v), t(".ew-crop-tool").on("click", "[data-method]", (function () {
                                    var e, n, o = t(this).data();
                                    if (y && o.method) {
                                        switch (o = t.extend({}, o), e = y.cropped, o.method) {
                                            case"rotate":
                                                e && v.viewMode > 0 && y.clear();
                                                break;
                                            case"getCroppedCanvas":
                                                "image/jpeg" === i && (o.option || (o.option = {}), o.option.fillColor = "#fff")
                                        }
                                        switch (n = y[o.method](o.option, o.secondOption), o.method) {
                                            case"rotate":
                                                e && v.viewMode > 0 && y.crop();
                                                break;
                                            case"scaleX":
                                            case"scaleY":
                                                t(this).data("option", -o.option);
                                                break;
                                            case"getCroppedCanvas":
                                                n ? (s && s(n.toDataURL(i)), r.closeDialog("#ew-crop-img")) : a.msg("裁剪失败", {
                                                    icon: 2,
                                                    anim: 6
                                                })
                                        }
                                    }
                                }))
                            }()
                        }
                    })
                }))
            }, r.util = {
                Convert_BD09_To_GCJ02: function (e) {
                    var t = 52.35987755982988, a = e.lng - .0065, i = e.lat - .006,
                        n = Math.sqrt(a * a + i * i) - 2e-5 * Math.sin(i * t),
                        o = Math.atan2(i, a) - 3e-6 * Math.cos(a * t);
                    return {lng: n * Math.cos(o), lat: n * Math.sin(o)}
                }, Convert_GCJ02_To_BD09: function (e) {
                    var t = 52.35987755982988, a = e.lng, i = e.lat,
                        n = Math.sqrt(a * a + i * i) + 2e-5 * Math.sin(i * t),
                        o = Math.atan2(i, a) + 3e-6 * Math.cos(a * t);
                    return {lng: n * Math.cos(o) + .0065, lat: n * Math.sin(o) + .006}
                }, animateNum: function (e, a, i, n) {
                    a = null == a || !0 === a || "true" === a, i = isNaN(i) ? 500 : i, n = isNaN(n) ? 100 : n;
                    var o = function (e, t) {
                        return t && /^[0-9]+.?[0-9]*$/.test(e) ? (e = e.toString()).replace(e.indexOf(".") > 0 ? /(\d)(?=(\d{3})+(?:\.))/g : /(\d)(?=(\d{3})+(?:$))/g, "$1,") : e
                    };
                    t(e).each((function () {
                        var e = t(this), l = e.data("num");
                        l || (l = e.text().replace(/,/g, ""), e.data("num", l));
                        var s = "INPUT,TEXTAREA".indexOf(e.get(0).tagName) >= 0, r = function (e) {
                            for (var t = "", a = 0; a < e.length; a++) {
                                if (!isNaN(e.charAt(a))) return t;
                                t += e.charAt(a)
                            }
                        }(l.toString()), c = function (e) {
                            for (var t = "", a = e.length - 1; a >= 0; a--) {
                                if (!isNaN(e.charAt(a))) return t;
                                t = e.charAt(a) + t
                            }
                        }(l.toString()), d = l.toString().replace(r, "").replace(c, "");
                        if (isNaN(1 * d) || "0" === d) return s ? e.val(l) : e.html(l), console.error("not a number");
                        var u = d.split("."), p = u[1] ? u[1].length : 0, y = 0, m = d;
                        Math.abs(1 * m) > 10 && (y = parseFloat(u[0].substring(0, u[0].length - 1) + (u[1] ? ".0" + u[1] : "")));
                        var f = (m - y) / n, v = 0, h = setInterval((function () {
                            var t = r + o(y.toFixed(p), a) + c;
                            s ? e.val(t) : e.html(t), y += f, v++, (Math.abs(y) >= Math.abs(1 * m) || v > 5e3) && (t = r + o(m, a) + c, s ? e.val(t) : e.html(t), clearInterval(h))
                        }), i / n)
                    }))
                }, deepClone: function (e) {
                    var t, a = r.util.isClass(e);
                    if ("Object" === a) t = {}; else {
                        if ("Array" !== a) return e;
                        t = []
                    }
                    for (var i in e) if (e.hasOwnProperty(i)) {
                        var n = e[i], o = r.util.isClass(n);
                        t[i] = "Object" === o || "Array" === o ? arguments.callee(n) : e[i]
                    }
                    return t
                }, isClass: function (e) {
                    return null === e ? "Null" : void 0 === e ? "Undefined" : Object.prototype.toString.call(e).slice(8, -1)
                }, fullTextIsEmpty: function (e) {
                    if (!e) return !0;
                    for (var t = ["img", "audio", "video", "iframe", "object"], a = 0; a < t.length; a++) if (e.indexOf("<" + t[a]) > -1) return !1;
                    var i = e.replace(/\s*/g, "");
                    return !i || (!(i = i.replace(/&nbsp;/gi, "")) || !(i = i.replace(/<[^>]+>/g, "")))
                }, removeStyle: function (e, a) {
                    "string" == typeof a && (a = [a]);
                    for (var i = 0; i < a.length; i++) t(e).css(a[i], "")
                }, scrollTop: function (e) {
                    t(e || "html,body").animate({scrollTop: 0}, 300)
                }, tpl: function (e, t, a, i) {
                    if (null == e || "string" != typeof e) return e;
                    t || (t = {}), a || (a = "{{"), i || (i = "}}");
                    var n = {
                        exp: function (e) {
                            return new RegExp(e, "g")
                        }, query: function (e, t, o) {
                            var l = ["#([\\s\\S])+?", "([^{#}])*?"][e || 0];
                            return n.exp((t || "") + a + l + i + (o || ""))
                        }, escape: function (e) {
                            return String(e || "").replace(/&(?!#?[a-zA-Z0-9]+;)/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/'/g, "&#39;").replace(/"/g, "&quot;")
                        }, error: function (e, t) {
                            console.error("Laytpl Error：" + e + "\n" + (t || ""))
                        }, parse: function (e, t) {
                            var o = e;
                            try {
                                var l = n.exp("^" + a + "#"), s = n.exp(i + "$");
                                return e = '"use strict";var view = "' + (e = e.replace(n.exp(a + "#"), a + "# ").replace(n.exp(i + "}"), "} " + i).replace(/\\/g, "\\\\").replace(n.exp(a + "!(.+?)!" + i), (function (e) {
                                    return e = e.replace(n.exp("^" + a + "!"), "").replace(n.exp("!" + i), "").replace(n.exp(a + "|" + i), (function (e) {
                                        return e.replace(/(.)/g, "\\$1")
                                    }))
                                })).replace(/(?="|')/g, "\\").replace(n.query(), (function (e) {
                                    return '";' + (e = e.replace(l, "").replace(s, "")).replace(/\\/g, "") + ';view+="'
                                })).replace(n.query(1), (function (e) {
                                    var t = '"+(';
                                    return e.replace(/\s/g, "") === a + i ? "" : (e = e.replace(n.exp(a + "|" + i), ""), /^=/.test(e) && (e = e.replace(/^=/, ""), t = '"+_escape_('), t + e.replace(/\\/g, "") + ')+"')
                                })).replace(/\r\n/g, '\\r\\n" + "').replace(/\n/g, '\\n" + "').replace(/\r/g, '\\r" + "')) + '";return view;', (e = new Function("d, _escape_", e))(t, n.escape)
                            } catch (e) {
                                return n.error(e, o), o
                            }
                        }
                    };
                    return n.parse(e, t)
                }, render: function (e) {
                    if ("string" == typeof e.url) return e.success = function (a) {
                        r.util.render(t.extend({}, e, {url: a}))
                    }, void("ajax" === e.ajax ? r.ajax(e) : r.req(e.url, e.where, e.success, e.method, e));
                    var a = r.util.tpl(e.tpl, e.url, e.open || i.tplOpen, e.close || i.tplClose);
                    t(e.elem).next("[ew-tpl-rs]").remove(), t(e.elem).after(a), t(e.elem).next().attr("ew-tpl-rs", ""), e.done && e.done(e.url)
                }
            }, r.lockScreen = function (e) {
                if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.lockScreen(e);
                console.log(e), e || (e = "/static/assets/libs/templets/tpl-lock-screen.html");
                var i = t("#ew-lock-screen-group");
                if (i.length > 0) i.fadeIn("fast"), r.isLockScreen = !0, r.putTempData("isLockScreen", r.isLockScreen, !0); else {
                    var n = a.load(2);
                    r.ajax({
                        url: e, dataType: "html", success: function (i) {
                            a.close(n), "string" == typeof i ? (t("body").append('<div id="ew-lock-screen-group">' + i + "</div>"), r.isLockScreen = !0, r.putTempData("isLockScreen", r.isLockScreen, !0), r.putTempData("lockScreenUrl", e, !0)) : (console.error(i), a.msg(JSON.stringify(i), {
                                icon: 2,
                                anim: 6
                            }))
                        }
                    })
                }
            }, r.unlockScreen = function (e) {
                if (window !== top && !r.isTop() && top.layui && top.layui.admin) return top.layui.admin.unlockScreen(e);
                var a = t("#ew-lock-screen-group");
                e ? a.remove() : a.fadeOut("fast"), r.isLockScreen = !1, r.putTempData("isLockScreen", null, !0)
            }, r.tips = function (e) {
                return a.tips(e.text, e.elem, {
                    tips: [e.direction || 1, e.bg || "#191a23"],
                    tipsMore: e.tipsMore,
                    time: e.time || -1,
                    success: function (a) {
                        var i = t(a).children(".layui-layer-content");
                        if ((e.padding || 0 === e.padding) && i.css("padding", e.padding), e.color && i.css("color", e.color), e.bgImg && i.css("background-image", e.bgImg).children(".layui-layer-TipsG").css("z-index", "-1"), e.fontSize && i.css("font-size", e.fontSize), e.offset) {
                            var n = e.offset.split(","), o = n[0], l = n.length > 1 ? n[1] : void 0;
                            o && t(a).css("margin-top", o), l && t(a).css("margin-left", l)
                        }
                    }
                })
            }, r.renderTpl = function (e) {
                function a(e) {
                    if (e) try {
                        return new Function("return " + e + ";")()
                    } catch (t) {
                        console.error(t + "\nlay-data: " + e)
                    }
                }

                layui.admin || (layui.admin = r), t(e || "[ew-tpl]").each((function () {
                    var e = t(this), i = t(this).data();
                    if (i.elem = e, i.tpl = e.html(), i.url = a(e.attr("ew-tpl")), i.headers = a(i.headers), i.where = a(i.where), i.done) try {
                        i.done = new Function("res", i.done)
                    } catch (e) {
                        console.error(e + "\nlay-data:" + i.done), i.done = void 0
                    }
                    r.util.render(i)
                }))
            }, r.on = function (e, t) {
                return layui.onevent.call(this, "admin", e, t)
            }, r.putSetting = function (e, t) {
                i[e] = t, r.putTempData(e, t, !0)
            }, r.recoverState = function () {
                if (r.getTempData("isLockScreen", !0) && r.lockScreen(r.getTempData("lockScreenUrl", !0)), i.defaultTheme && r.changeTheme(i.defaultTheme, window, !0, !0), i.closeFooter && t("body").addClass("close-footer"), void 0 !== i.navArrow) {
                    var e = t(l + ">.layui-nav-tree");
                    e.removeClass("arrow2 arrow3"), i.navArrow && e.addClass(i.navArrow)
                }
                i.pageTabs && "true" == i.tabAutoRefresh && t(o).attr("lay-autoRefresh", "true")
            }, r.on = function (e, t) {
                return layui.onevent.call(this, "admin", e, t)
            };
            var c = ".layui-layout-admin.admin-nav-mini>.layui-side .layui-nav .layui-nav-item";
            t(document).on("mouseenter", c + "," + c + " .layui-nav-child>dd", (function () {
                if (r.getPageWidth() > 768) {
                    var e = t(this), a = e.find(">.layui-nav-child");
                    if (a.length > 0) {
                        e.addClass("admin-nav-hover"), a.css("left", e.offset().left + e.outerWidth());
                        var i = e.offset().top;
                        i + a.outerHeight() > r.getPageHeight() && ((i = i - a.outerHeight() + e.outerHeight()) < 60 && (i = 60), a.addClass("show-top")), a.css("top", i), a.addClass("ew-anim-drop-in")
                    } else e.hasClass("layui-nav-item") && r.tips({
                        elem: e,
                        text: e.find("cite").text(),
                        direction: 2,
                        offset: "12px"
                    })
                }
            })).on("mouseleave", c + "," + c + " .layui-nav-child>dd", (function () {
                a.closeAll("tips");
                var e = t(this);
                e.removeClass("admin-nav-hover");
                var i = e.find(">.layui-nav-child");
                i.removeClass("show-top ew-anim-drop-in"), i.css({left: "auto", top: "auto"})
            })), t(document).on("click", "*[ew-event]", (function () {
                var e = r.events[t(this).attr("ew-event")];
                e && e.call(this, t(this))
            })), t(document).on("mouseenter", "*[lay-tips]", (function () {
                var e = t(this);
                r.tips({
                    elem: e,
                    text: e.attr("lay-tips"),
                    direction: e.attr("lay-direction"),
                    bg: e.attr("lay-bg"),
                    offset: e.attr("lay-offset"),
                    padding: e.attr("lay-padding"),
                    color: e.attr("lay-color"),
                    bgImg: e.attr("lay-bgImg"),
                    fontSize: e.attr("lay-fontSize")
                })
            })).on("mouseleave", "*[lay-tips]", (function () {
                a.closeAll("tips")
            })), t(document).on("click", ".form-search-expand,[search-expand]", (function () {
                var e = t(this), a = e.parents(".layui-form").first(), i = e.data("expand"),
                    n = e.attr("search-expand");
                if (void 0 === i || !0 === i) {
                    i = !0, e.data("expand", !1), e.html('收起 <i class="layui-icon layui-icon-up"></i>');
                    var o = a.find(".form-search-show-expand");
                    o.attr("expand-show", ""), o.removeClass("form-search-show-expand")
                } else i = !1, e.data("expand", !0), e.html('展开 <i class="layui-icon layui-icon-down"></i>'), a.find("[expand-show]").addClass("form-search-show-expand");
                n && new Function("d", n)({expand: i, elem: e})
            })), t(document).on("click.ew-sel-fixed", ".ew-select-fixed .layui-form-select .layui-select-title", (function () {
                var e = t(this), a = e.parent().children("dl"), i = e.offset().top, n = e.outerWidth(),
                    o = e.outerHeight(), l = t(document).scrollTop(), s = a.outerWidth(), c = a.outerHeight(),
                    d = i + o + 5 - l, u = e.offset().left;
                d + c > r.getPageHeight() && (d = d - c - o - 10), u + s > r.getPageWidth() && (u = u - s + n), a.css({
                    left: u,
                    top: d,
                    "min-width": n
                })
            })), r.hideFixedEl = function () {
                t(".ew-select-fixed .layui-form-select").removeClass("layui-form-selected layui-form-selectup"), t("body>.layui-laydate").remove()
            }, t(document).on("click", ".layui-nav-tree>.layui-nav-item a", (function () {
                var e = t(this), a = e.siblings(".layui-nav-child"), i = e.parent();
                if (0 !== a.length && !i.hasClass("admin-nav-hover") && (i.hasClass("layui-nav-itemed") ? a.css("display", "none").slideDown("fast", (function () {
                    t(this).css("display", "")
                })) : a.css("display", "block").slideUp("fast", (function () {
                    t(this).css("display", "")
                })), "_all" === e.parents(".layui-nav").attr("lay-shrink"))) {
                    var n = e.parent().siblings(".layui-nav-itemed");
                    n.children(".layui-nav-child").css("display", "block").slideUp("fast", (function () {
                        t(this).css("display", "")
                    })), n.removeClass("layui-nav-itemed")
                }
            })), t('.layui-nav-tree[lay-shrink="all"]').attr("lay-shrink", "_all"), t(document).on("click", ".layui-collapse>.layui-colla-item>.layui-colla-title", (function () {
                var e = t(this), a = e.siblings(".layui-colla-content"), i = e.parent().parent(),
                    n = a.hasClass("layui-show");
                if (n ? a.removeClass("layui-show").slideDown("fast").addClass("layui-show") : a.css("display", "block").slideUp("fast", (function () {
                    t(this).css("display", "")
                })), e.children(".layui-colla-icon").html("&#xe602;").css({
                    transition: "all .3s",
                    transform: "rotate(" + (n ? "90deg" : "0deg") + ")"
                }), "_all" === i.attr("lay-shrink")) {
                    var o = i.children(".layui-colla-item").children(".layui-colla-content.layui-show").not(a);
                    o.css("display", "block").slideUp("fast", (function () {
                        t(this).css("display", "")
                    })), o.removeClass("layui-show"), o.siblings(".layui-colla-title").children(".layui-colla-icon").html("&#xe602;").css({
                        transition: "all .3s",
                        transform: "rotate(0deg)"
                    })
                }
            })), t(".layui-collapse[lay-accordion]").attr("lay-shrink", "_all").removeAttr("lay-accordion"), a.oldTips = a.tips, a.tips = function (e, i, n) {
                var o;
                if (t(i).length > 0 && t(i).parents(".layui-form").length > 0 && (t(i).is("input") || t(i).is("textarea") ? o = t(i) : (t(i).hasClass("layui-form-select") || t(i).hasClass("layui-form-radio") || t(i).hasClass("layui-form-checkbox") || t(i).hasClass("layui-form-switch")) && (o = t(i).prev())), !o) return a.oldTips(e, i, n);
                n.tips = [o.attr("lay-direction") || 3, o.attr("lay-bg") || "#ff4c4c"], setTimeout((function () {
                    n.success = function (e) {
                        t(e).children(".layui-layer-content").css("padding", "6px 12px")
                    }, a.oldTips(e, i, n)
                }), 100)
            }, t(document).on("click", "*[ew-href]", (function () {
                var e = t(this), a = e.attr("ew-href");
                if (a && "#" !== a) {
                    if (0 === a.indexOf("javascript:")) return new Function(a.substring(11))();
                    var i = e.attr("ew-title") || e.text(), n = e.data("window");
                    n = n ? r.strToWin(n) : top;
                    var o = e.attr("ew-end");
                    try {
                        o = o ? new Function(o) : void 0
                    } catch (e) {
                        console.error(e)
                    }
                    n.layui && n.layui.index ? n.layui.index.openTab({
                        title: i || "",
                        url: a,
                        end: o
                    }) : location.href = a
                }
            })), layui.contextMenu || t(document).off("click.ctxMenu").on("click.ctxMenu", (function () {
                try {
                    for (var e = top.window.frames, t = 0; t < e.length; t++) {
                        var a = e[t];
                        try {
                            a.layui && a.layui.jquery && a.layui.jquery("body>.ctxMenu").remove()
                        } catch (e) {
                        }
                    }
                    try {
                        top.layui && top.layui.jquery && top.layui.jquery("body>.ctxMenu").remove()
                    } catch (e) {
                    }
                } catch (e) {
                }
            })), i = t.extend({
                pageTabs: !0,
                cacheTab: !0,
                openTabCtxMenu: !0,
                maxTabNum: 20,
                tableName: "easyweb-iframe",
                apiNoCache: !0,
                ajaxSuccessBefore: function (e, t, a) {
                    return !r.ajaxSuccessBefore || r.ajaxSuccessBefore(e, t, a)
                },
                getAjaxHeaders: function (e, t, a) {
                    return r.getAjaxHeaders ? r.getAjaxHeaders(e, t, a) : []
                }
            }, i);
            var d = r.getTempData(!0);
            if (d) for (var u = ["pageTabs", "cacheTab", "defaultTheme", "navArrow", "closeFooter", "tabAutoRefresh"], p = 0; p < u.length; p++) void 0 !== d[u[p]] && (i[u[p]] = d[u[p]]);
            r.recoverState(), r.renderTpl(), r.setter = i, layui.device().ios && t("body").addClass("ios-iframe-body"), e("admin", r)
        }))
    }
});