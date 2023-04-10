package com.javaweb.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaweb.system.entity.Target;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客资指标结果 Mapper 接口
 * </p>
 *
 * @author leavin
 * @since 2023-04-07
 */
public interface TargetMapper extends BaseMapper<Target> {

    @Select("select target_code,sum(target_value) target_value from sys_target\n" +
            "where user_id = #{userId}\n" +
            "and date_format(data_time,'%Y-%m-%d') <= #{endDt}\n" +
            "and date_format(data_time,'%Y-%m-%d') >= #{startDt}\n" +
            "group by target_code ")
    List<Map<String, Object>> selectBaseListByUser(@Param("userId")Integer id, @Param("startDt")String startDt, @Param("endDt")String endDt);

    @Select("select target_code,sum(target_value) target_value from sys_target \n" +
            "where date_format(data_time,'%Y-%m-%d') <= #{endDt}\n" +
            "and date_format(data_time,'%Y-%m-%d') >= #{startDt}\n" +
            "group by target_code\n")
    List<Map<String, Object>> selectBaseList(@Param("startDt")String startDt, @Param("endDt")String endDt);
}
