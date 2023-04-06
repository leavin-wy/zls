package com.javaweb.admin.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 图片标注信息导出Dto
 */
@Data
public class ImageExportDto extends BaseRowModel {

    /**
     * 图片地址
     */
    @ExcelProperty(value = "图片地址")
    private String imageUrl;
    /**
     * 图片分组
     */
    @ExcelProperty(value = "图片分组")
    private String imageGroup;
    /**
     * 裂纹
     */
    @ExcelProperty(value = "裂纹缼欠等级")
    private String bmqq1;
    /**
     * 弧坑裂纹
     */
    @ExcelProperty(value = "弧坑裂纹缼欠等级")
    private String bmqq2;
    /**
     * 表面气孔
     */
    @ExcelProperty(value = "表面气孔缼欠等级")
    private String bmqq3;
    /**
     * 开口弧坑
     */
    @ExcelProperty(value = "开口弧坑缼欠等级")
    private String bmqq4;
    /**
     * 未熔合
     */
    @ExcelProperty(value = "未熔合缼欠等级")
    private String bmqq5;
    /**
     * 微观未熔合
     */
    @ExcelProperty(value = "微观未熔合缼欠等级")
    private String bmqq6;
    /**
     * 根部熔深不够
     */
    @ExcelProperty(value = "根部熔深不够缼欠等级")
    private String bmqq7;
    /**
     * 盖面咬边
     */
    @ExcelProperty(value = "盖面咬边缼欠等级")
    private String bmqq8;	/**
     * 根部咬边
     */
    @ExcelProperty(value = "根部咬边缼欠等级")
    private String bmqq9;
    /**
     * 余高过大
     */
    @ExcelProperty(value = "余高过大缼欠等级")
    private String bmqq10;
    /**
     * 盖面余高过大
     */
    @ExcelProperty(value = "盖面余高过大缼欠等级")
    private String bmqq11;
    /**
     * 根部余高过大
     */
    @ExcelProperty(value = "根部余高过大缼欠等级")
    private String bmqq12;
    /**
     * 焊缝过渡过陡
     */
    @ExcelProperty(value = "焊缝过渡过陡缼欠等级")
    private String bmqq13;
    /**
     * 焊缝金属溢出
     */
    @ExcelProperty(value = "焊缝金属溢出缼欠等级")
    private String bmqq14;
    /**
     * 盖面凹陷
     */
    @ExcelProperty(value = "盖面凹陷缼欠等级")
    private String bmqq15;
    /**
     * 根部填充不足
     */
    @ExcelProperty(value = "根部填充不足缼欠等级")
    private String bmqq16;
    /**
     * 烧穿
     */
    @ExcelProperty(value = "烧穿缼欠等级")
    private String bmqq17;
    /**
     * 角焊缝过渡不对称
     */
    @ExcelProperty(value = "角焊缝过渡不对称缼欠等级")
    private String bmqq18;

    /**
     * 根部凹陷
     */
    @ExcelProperty(value = "根部凹陷缼欠等级")
    private String bmqq19;

    /**
     * 根部弥散气孔
     */
    @ExcelProperty(value = "根部弥散气孔缼欠等级")
    private String bmqq20;

    /**
     * 接头缼欠
     */
    @ExcelProperty(value = "接头缼欠缼欠等级")
    private String bmqq21;

    /**
     * 角焊缝厚度过小
     */
    @ExcelProperty(value = "角焊缝厚度过小缼欠等级")
    private String bmqq22;

    /**
     * 角焊缝厚度过大
     */
    @ExcelProperty(value = "角焊缝厚度过大缼欠等级")
    private String bmqq23;

    /**
     * 引弧点
     */
    @ExcelProperty(value = "引弧点缼欠等级")
    private String bmqq24;

    /**
     * 焊接飞溅
     */
    @ExcelProperty(value = "焊接飞溅缼欠等级")
    private String bmqq25;

    /**
     * 错边
     */
    @ExcelProperty(value = "错边缼欠等级")
    private String jhxzqq1;

    /**
     * 角变形
     */
    @ExcelProperty(value = "角变形缼欠等级")
    private String jhxzqq2;

    /**
     * 角焊缝
     */
    @ExcelProperty(value = "角焊缝缼欠等级")
    private String jhxzqq3;

}
