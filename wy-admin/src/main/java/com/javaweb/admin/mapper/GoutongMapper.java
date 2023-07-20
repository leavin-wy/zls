package com.javaweb.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.javaweb.admin.entity.Goutong;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 沟通记录 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
public interface GoutongMapper extends BaseMapper<Goutong> {

    IPage<Goutong> selectPage(IPage<Goutong> page, @Param("ew") QueryWrapper<Goutong> queryWrapper);
}
