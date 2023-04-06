package com.javaweb.system.service;

import com.javaweb.system.entity.Dep;
import com.javaweb.common.common.IBaseService;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author leavin
 * @since 2020-05-03
 */
public interface IDepService extends IBaseService<Dep> {

    /**
     * 根据部门ID获取部门名称
     * @param depId 部门ID
     * @param delimiter 拼接字符
     * @return
     */
    String getDepNameByDepId(Integer depId, String delimiter);

}