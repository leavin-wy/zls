package com.javaweb.system.vo;

import lombok.Data;

/**
 * <p>
 * 客资指标结果列表Vo
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
@Data
public class TargetListVo {

    /**
     * 客资指标结果ID
     */
    private Integer id;

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