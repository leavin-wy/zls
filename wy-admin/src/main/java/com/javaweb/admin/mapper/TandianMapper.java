package com.javaweb.admin.mapper;

import com.javaweb.admin.entity.Tandian;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 探店记录 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2023-04-01
 */
public interface TandianMapper extends BaseMapper<Tandian> {
    @Update("update sys_customer set td_num = td_num+1 where id = #{custId}")
    int updateTdNumAdd(@Param("custId")int custId);

    @Update("update sys_customer set td_num = td_num-1 where id = #{custId}")
    int updateTdNumReduce(@Param("custId")int custId);
}
