package com.javaweb.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.javaweb.common.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 客资指标结果
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_target")
public class Target extends BaseEntity {

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 指标代码
     */
    private String targetCode;

    /**
     * 指标名称
     */
    private String targetName;

    /**
     * 指标值
     */
    private Integer targetValue;

    /**
     * 数据日期
     */
    private String dataTime;

}