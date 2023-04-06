package com.javaweb.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaweb.common.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 * 图片标注信息
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_image_mark")
public class ImageMark extends BaseEntity {

    /**
     * 图片id
     */
    private Integer imgId;

    /**
     * 图片缺陷类型
     */
    private String imgDefectType;

    /**
     * 图片缺陷等级: 1=B(高) 2=C(中) 3=D(低)
     */
    private String imgDefectLevel;

    /**
     * 图片缺陷(多个规则逗号隔开)
     */
    private String imgDefect;

    /**
     * 备注
     */
    private String note;

}