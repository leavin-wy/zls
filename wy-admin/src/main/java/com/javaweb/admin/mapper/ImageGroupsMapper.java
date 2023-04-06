package com.javaweb.admin.mapper;

import com.javaweb.admin.entity.ImageGroups;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 图片分组信息 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2021-01-11
 */
public interface ImageGroupsMapper extends BaseMapper<ImageGroups> {
    @Select("select count(1) from sys_images where img_group = #{id}")
    Integer checkGroupImg(@Param("id") Integer id);
}
