package com.javaweb.admin.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 图片标注信息 模块常量
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
public class ImageMarkConstant {

    public static final String DEFECT_TYPE_BMQQ = "bmqq";
    public static final String DEFECT_TYPE_JHXZQQ = "jhxzqq";
    public static final String[] DEFECTTYPES = new String[]{"bmqq","jhxzqq"};

    /**
     * 图片缺陷等级
     */
    public static Map<String, String> IMAGEMARK_IMGDEFECTLEVEL_LIST = new HashMap<String, String>() {
    {
            put("1", "B(高)");
            put("2", "C(中)");
            put("3", "D(低)");
    }
    };

    /**
     * 表面缺欠--缺陷
     */
    public static Map<String, String> IMAGEMARK_IMGDEFECT_BMQQ_LIST = new HashMap<String, String>() {
        {
            put("1", "裂纹");
            put("2", "弧坑裂纹");
            put("3", "表面气孔");
            put("4", "开口弧坑");
            put("5", "未熔合");
            put("6", "微观未熔合");
            put("7", "根部熔深不够");
            put("8", "盖面咬边");
            put("9", "根部咬边");
            put("10", "余高过大");
            put("11", "盖面余高过大");
            put("12", "根部余高过大");
            put("13", "焊缝过渡过陡");
            put("14", "焊缝金属溢出");
            put("15", "盖面凹陷");
            put("16", "根部填充不足");
            put("17", "烧穿");
            put("18", "角焊缝过渡不对称");
            put("19", "根部凹陷");
            put("20", "根部弥散气孔");
            put("21", "接头缼欠");
            put("22", "角焊缝厚度过小");
            put("23", "角焊缝厚度过大");
            put("24", "引弧点");
            put("25", "焊接飞溅");
        }
    };

    /**
     * 几何形状缺欠--缺陷
     */
    public static Map<String, String> IMAGEMARK_IMGDEFECT_JHXZQQ_LIST = new HashMap<String, String>() {
        {
            put("1", "错边");
            put("2", "角变形");
            put("3", "角焊缝");
        }
    };
}