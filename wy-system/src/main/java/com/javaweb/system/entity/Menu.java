package com.javaweb.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 菜单
 * </p>
 *
 * @author leavin
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends BaseEntity {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * URL地址
     */
    private String url;

    /**
     * 参数
     */
    private String param;

    /**
     * 上级ID
     */
    private Integer pid;

    /**
     * 类型：1模块 2导航 3菜单 4节点
     */
    private Integer type;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否显示：1显示 2不显示
     */
    private Integer status;

    /**
     * 是否公共：1是 2否
     */
    private Integer isPublic;

    /**
     * 菜单备注
     */
    private String note;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 菜单节点
     */
    @TableField(exist = false)
    private String funcIds;

}