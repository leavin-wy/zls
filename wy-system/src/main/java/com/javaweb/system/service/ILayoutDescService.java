package com.javaweb.system.service;

import com.javaweb.system.entity.LayoutDesc;
import com.javaweb.common.common.IBaseService;

/**
 * <p>
 * 布局描述 服务类
 * </p>
 *
 * @author leavin
 * @since 2020-04-20
 */
public interface ILayoutDescService extends IBaseService<LayoutDesc> {

    /**
     * 根据ID获取位置描述
     * @param itemId 页面位置ID
     * @param locId 位置ID
     * @return
     */
    LayoutDesc getLocDescById(Integer itemId, Integer locId);

}