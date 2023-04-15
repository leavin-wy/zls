package com.javaweb.system.service;

import com.javaweb.common.utils.JsonResult;

/**
 * 首页指标接口
 * @author leavin
 * @since 2023/4/09 13:57
 */
public interface IIndexService {

    /**
     * 首页
     * @param flag
     * @return
     */
    JsonResult maininitbase(String flag);


    JsonResult maininitxzfb(String flag,String[] codes);
}
