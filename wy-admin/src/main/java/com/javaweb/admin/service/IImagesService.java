package com.javaweb.admin.service;

import com.javaweb.admin.entity.Images;
import com.javaweb.common.common.IBaseService;
import com.javaweb.common.utils.JsonResult;

import java.util.Map;

/**
 * <p>
 * 图片信息 服务类
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
public interface IImagesService extends IBaseService<Images> {
    public JsonResult upload(Map<String, Object> map) ;
}