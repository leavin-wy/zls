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
 * 图片信息
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_images")
public class Images extends BaseEntity {

    private Integer imgGroup;

    /**
     * 图片
     */
    private String imgUrl;

    /**
     * 图片是否标注： 1=已标注 2=未标注
     */
    private String imgMark;

    /**
     * 图片缺陷(多个规则逗号“,”隔开)
     */
    private String imgDefect;

    /**
     * 备注
     */
    private String note;

    /**
     * 显示顺序
     */
    private Integer sort;

}