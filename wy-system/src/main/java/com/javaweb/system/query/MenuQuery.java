package com.javaweb.system.query;

import com.javaweb.common.common.BaseQuery;
import lombok.Data;

/**
 * <p>
 * 菜单查询条件
 * </p>
 *
 * @author leavin
 * @since 2020-05-07
 */
@Data
public class MenuQuery extends BaseQuery {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 类型：1模块 2导航 3菜单 4节点
     */
    private Integer type;

    /**
     * 是否显示：1显示 2不显示
     */
    private Integer status;

    /**
     * 是否公共：1是 2否
     */
    private Integer isPublic;

}
