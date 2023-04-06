package com.javaweb.admin.service;

import com.javaweb.admin.entity.ImageMark;
import com.javaweb.common.common.IBaseService;
import com.javaweb.common.utils.JsonResult;

import java.util.Map;

/**
 * <p>
 * 图片标注信息 服务类
 * </p>
 *
 * @author leavin
 * @since 2021-01-12
 */
public interface IImageMarkService extends IBaseService<ImageMark> {
    JsonResult saveMark(Map<String, Object> param);
    Map<String,String> queryMark(Integer imgId);
    JsonResult delImageInfo(Integer imgId);
}