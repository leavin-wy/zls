package com.javaweb.admin.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 图片信息 模块常量
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
public class ImagesConstant {

    /**
     * 图片是否标注
     */
    public static Map<String, String> IMAGES_IMGMARK_LIST = new HashMap<String, String>() {
    {
            put("1", "已标注");
            put("2", "未标注");
    }
    };
}