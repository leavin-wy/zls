package com.javaweb.admin.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 沟通记录 模块常量
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
public class GoutongConstant {
    /**
     * 回复标志
     */
    public static Map<Object, String> GOUTONG_REPLY_LIST = new HashMap<Object, String>() {
        {
            put(1, "未回复");
            put(2, "已回复");
        }
    };
}