package com.javaweb.admin.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客户信息 模块常量
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
public class CustomerConstant {

    /**
     * 性别
     */
    public static Map<Object, String> CUSTOMER_SEX_LIST = new HashMap<Object, String>() {
    {
        put(1, "男");
        put(2, "女");
        put(3, "未知");
    }
    };
    /**
     * 客户类型
     */
    public static Map<Object, String> CUSTOMER_CUSTTYPE_LIST = new HashMap<Object, String>() {
    {
        put(1, "A");
        put(2, "B");
        put(3, "C");
        put(4, "D");
        put(5, "E");
        put(6, "S");
        put(7, "成交");
        put(8, "F");
        put(9, "C+");
        put(10,"成交未入读");
    }
    };
    /**
     * 渠道
     */
    public static Map<Object, String> CUSTOMER_SOURCE_LIST = new HashMap<Object, String>() {
    {
        put(1, "美团");
        put(2, "扫街");
        put(3, "自然到店");
        put(4, "转介绍");
        put(5, "其他");
        put(6, "新生堂");
        put(7, "影耀");
    }
    };
}