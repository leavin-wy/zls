package com.javaweb.system.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客资指标结果 模块常量
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
public class TargetConstant {
    //客户来源指标
    public static Map<Integer, String> CUSTOMER_SOURCE_LIST = new HashMap<Integer, String>() {
        {
            put(1, "1001|美团");//美团
            put(2, "1002|扫街");//扫街
            put(3, "1003|自然到店");//自然到店
            put(4, "1004|转介绍");//转介绍
            put(5, "1005|其他");//其他
            put(6, "1006|新生堂");//新生堂
            put(7, "1007|影耀");//影耀
            //3006  当日新增总数
        }
    };
}